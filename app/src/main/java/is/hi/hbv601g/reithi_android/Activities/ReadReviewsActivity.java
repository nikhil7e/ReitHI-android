package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Entities.Review;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class ReadReviewsActivity extends AppCompatActivity {

    private static final String TAG = "ReadReviewsActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_reviews);

        mParserService = ParserService.getInstance();
        String courseString = getIntent().getExtras().getString("course");
        Log.d(TAG, courseString);

        LinearLayout allReviews = findViewById(R.id.all_Reviews);
        Type courseListType = new TypeToken<List<Course>>(){}.getType();
        List<Course> courses = (List<Course>) (Object) mParserService.parse(courseString, courseListType);
        Course course = courses.get(0);

        TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
        mCourseNameTitle.setText(course.getName());


        List<Review> reviews = course.getReviews();
        Log.d(TAG, "num reviews total "+course.getTotalReviews()); //rétt tala
        Log.d(TAG, "num reviews "+reviews.size()); //0 for some reason
        for (Review review:reviews) {
            TextView textView = new TextView(this);
            textView.setText(review.getComment());
            allReviews.addView(textView);
        }

    }

}
