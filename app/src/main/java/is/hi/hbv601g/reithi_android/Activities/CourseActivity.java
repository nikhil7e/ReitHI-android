package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "courseActivity";
    private static final String[] headings = {"Overall Score", "Difficulty","Material","Workload","Teaching Quality"};

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private CourseService mCourseService;

    private Button mReviewButton;
    private Button mReadReviewsButton;

    private Course mCourse;
    private String mCourseString;
    private TextView[] scoreTextviews;

    private boolean coldStart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mShouldCallOnResume = false;
        setContentView(R.layout.activity_course);

        mParserService = ParserService.getInstance();
        mCourseString = getIntent().getExtras().getString("course");
        mCourseService = new CourseService(this);
        Log.d(TAG, mCourseString);

        scoreTextviews = new TextView[]{findViewById(R.id.overallscoretext), findViewById(R.id.difficultytext), findViewById(R.id.materialtext), findViewById(R.id.workloadtext), findViewById(R.id.teachingqualitytext)};

        mCourse = (Course) mParserService.parseObject(mCourseString, Course.class);
        TextView mCourseNameTitle = findViewById(R.id.courseName);
        mCourseNameTitle.setText(mCourse.getName());


        mReviewButton = findViewById(R.id.review_button);
        mReviewButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, ReviewPageActivity.class);
            intent.putExtra("course", mCourseString);
            startActivity(intent);
        });

        mReadReviewsButton = findViewById(R.id.read_reviews_button);
        mReadReviewsButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, ReadReviewsActivity.class);
            intent.putExtra("course", mCourseString);
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

                    Log.d(TAG, json);
                    loadData();
                }
            }, "/getcoursebyid/?id=" + mCourse.getID());
        }
        else{
            coldStart = false;
            loadData();
        }

    }

    private void loadData(){
        Double[] ratings = {mCourse.getTotalOverall(), mCourse.getTotalDifficulty(), mCourse.getTotalCourseMaterial(), mCourse.getTotalWorkload(), mCourse.getTotalTeachingQuality()};
        for (int i = 0; i<scoreTextviews.length; i++){
            double rating = ratings[i] / mCourse.getTotalReviews();
            TextView score = scoreTextviews[i];
            score.setText(headings[i] +": " + rating);
        }
    }

}
