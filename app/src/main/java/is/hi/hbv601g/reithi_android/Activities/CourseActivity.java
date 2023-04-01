package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;
import is.hi.hbv601g.reithi_android.Services.ParserService;
import is.hi.hbv601g.reithi_android.Services.UserService;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "courseActivity";
    private static final String[] headings = {"Overall Score", "Difficulty", "Material", "Workload", "Teaching Quality"};

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private CourseService mCourseService;

    private Button mReviewButton;
    private Button mReadReviewsButton;

    private Course mCourse;
    private String mCourseString;
    private TextView[] scoreTextviews;
    TextView mCourseNameTitle;
    private TextView mSemesterTextView;
    private TextView mCreditsTextView;
    private TextView mSchoolTextView;

    private UserService mUserService;

    private boolean coldStart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mShouldCallOnResume = false;
        setContentView(R.layout.activity_course);

        mParserService = ParserService.getInstance();
        mCourseString = getIntent().getExtras().getString("course");
        mCourseService = new CourseService(this);
        mUserService = new UserService(this);
        Log.d(TAG, mCourseString);

        scoreTextviews = new TextView[]{findViewById(R.id.overallscoretext), findViewById(R.id.difficultytext), findViewById(R.id.materialtext), findViewById(R.id.workloadtext), findViewById(R.id.teachingqualitytext)};



        mCourse = (Course) mParserService.parseObject(mCourseString, Course.class);
        mCourseNameTitle = findViewById(R.id.courseName);
        mSemesterTextView = findViewById(R.id.semesterTextView);
        mCreditsTextView = findViewById(R.id.creditsTextView);
        mSchoolTextView = findViewById(R.id.schoolTextView);
        mCourseNameTitle.setText(mCourse.getName());
        mSemesterTextView.setText(mCourse.getSemester());
        mCreditsTextView.setText(mCourse.getCredits().toString());
        mSchoolTextView.setText(mCourse.getSchool());
        mReviewButton = findViewById(R.id.review_button);

        mReadReviewsButton = findViewById(R.id.read_reviews_button);
        mReadReviewsButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, ReadReviewsActivity.class);
            intent.putExtra("course", mCourseString);
            intent.putExtra("context", "course");
            startActivity(intent);
        });

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();
        coldStart = true;
    }

    protected void onResume() {

        super.onResume();
        if (!coldStart) {
            mCourseService.searchCoursesGET(new NetworkCallback<String>() {
                @Override
                public void onFailure(String errorString) {
                    // Handle the error
                }

                @Override
                public void onSuccess(String json) {
                    // Parse the response JSON and update the Course object
                    /*Type listType = new TypeToken<List<Course>>() {
                    }.getType();*/
                    mCourseString = json;
                    mCourse = (Course) (Object) mParserService.parseObject(json, Course.class);
                    checkReviewButtonAccess();

                    Log.d(TAG, json);
                    loadData();
                }
            }, "/getcoursebyid/?id=" + mCourse.getID());
        } else {
            coldStart = false;
            checkReviewButtonAccess();
            loadData();
        }

    }
    private void checkReviewButtonAccess(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        String userString = sharedPreferences.getString("loggedInUser", "");
        User user = (User) mParserService.parseObject(userString, User.class);

        if (userString.isEmpty()) {
            mReviewButton.setOnClickListener(v -> {
                Toast.makeText(getApplicationContext(), "Please log in to add a review", Toast.LENGTH_SHORT).show();
            });
            mReviewButton.setBackgroundColor(Color.GRAY);
        } else if (mCourse.getReviews().stream()
                .anyMatch(review -> review.getUserName().equals(user.getUserName()))) {
            mReviewButton.setOnClickListener(v -> {
                Toast.makeText(getApplicationContext(), "You have already reviewed this course", Toast.LENGTH_SHORT).show();
            });
            mReviewButton.setBackgroundColor(Color.GRAY);
        } else {
            mReviewButton.setBackgroundColor(ContextCompat.getColor(this, R.color.reithi_litur));
            mReviewButton.setOnClickListener(v -> {
                Intent intent = new Intent(CourseActivity.this, ReviewPageActivity.class);
                intent.putExtra("course", mCourseString);
                startActivity(intent);
            });
        }
    }
    private void loadData() {
        Double[] ratings = {mCourse.getTotalOverall(), mCourse.getTotalDifficulty(), mCourse.getTotalCourseMaterial(), mCourse.getTotalWorkload(), mCourse.getTotalTeachingQuality()};
        for (int i = 0; i < scoreTextviews.length; i++) {
            double rating = ratings[i] / mCourse.getTotalReviews();
            TextView score = scoreTextviews[i];
            score.setText(headings[i] + ": " + rating);
        }
    }

}
