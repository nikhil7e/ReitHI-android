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

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "coursePageActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private Button mReviewButton;
    private Button mReadReviewsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mParserService = ParserService.getInstance();
        String courseString = getIntent().getExtras().getString("course");
        Log.d(TAG, courseString);


//        Type courseListType = new TypeToken<List<Course>>(){}.getType();
//        List<Course> courses = (List<Course>) (Object) mParserService.parse(courseString, courseListType);
//        Course course = courses.get(0);
        Course course = (Course) mParserService.parseObject(courseString, Course.class);
        TextView mCourseNameTitle = findViewById(R.id.courseName);
        mCourseNameTitle.setText(course.getName());


        Double[] ratings = {course.getTotalOverall(), course.getTotalDifficulty(), course.getTotalCourseMaterial(), course.getTotalWorkload(), course.getTotalTeachingQuality()};
        String[] headings = {"Overall Score", "Difficulty","Material","Workload","Teaching Quality"};
        LinearLayout ratingLayout = findViewById(R.id.scores);
        for (int i = 0; i<ratings.length; i++){
            double rating = ratings[i] / course.getTotalReviews();
            TextView score = new TextView(this);
            score.setText(headings[i] +": " + rating);
            ratingLayout.addView(score);
        }


        mReviewButton = findViewById(R.id.review_button);
        mReviewButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, ReviewPageActivity.class);
            intent.putExtra("course", courseString);
            startActivity(intent);
        });

        mReadReviewsButton = findViewById(R.id.read_reviews_button);
        mReadReviewsButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, ReadReviewsActivity.class);
            intent.putExtra("course", courseString);
            startActivity(intent);
        });

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();




    }
//    @Override
//    public void onCreate( Bundle savedInstanceState) {
//        mParserService = ParserService.getInstance();
//        mSearchResults = view.findViewById(R.id.search_results);
//        Type courseType = new TypeToken<Course>() { }.getType();
//        String results = requireArguments().getString("searchResult");
//        List<Course> courseList = (List<Course>)(Object) mParserService.parse(results, listType);
//        Context context = getActivity();
//        if (courseList.size() == 0) {
//            TextView noCourses = new TextView(context);
//            noCourses.setText("No Courses Found!");
//            mSearchResults.addView(noCourses);
//        }
//
//        for (Course course : courseList) {
//            LinearLayout searchResultLayout = new LinearLayout(context);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            searchResultLayout.setLayoutParams(layoutParams);
//            TextView searchResultTextView = new TextView(context);
//            searchResultTextView.setText((course.getCredits()).toString() + " credits");
//            searchResultLayout.addView(searchResultTextView);
//            Button searchResultButton = new Button(context);
//            searchResultButton.setText(course.getName());
//            searchResultButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, CourseActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//            searchResultLayout.addView(searchResultButton);
//            mSearchResults.addView(searchResultLayout);
//        }
//
//    }
}
