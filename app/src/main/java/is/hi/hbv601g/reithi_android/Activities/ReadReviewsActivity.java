package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import is.hi.hbv601g.reithi_android.Entities.Review;
import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.Fragments.SearchResultFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;
import is.hi.hbv601g.reithi_android.Services.ParserService;
import is.hi.hbv601g.reithi_android.Services.ReviewService;
import is.hi.hbv601g.reithi_android.Services.UserService;

public class ReadReviewsActivity extends AppCompatActivity {

    private static final String TAG = "ReadReviewsActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private ReviewService mReviewService;
    private UserService mUserService;
    private CourseService mCourseService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_reviews);

        mParserService = ParserService.getInstance();
        mReviewService = new ReviewService(this);
        mUserService = new UserService(this);
        mCourseService = new CourseService(this);
        String context = getIntent().getExtras().getString("context");
        Log.d(TAG, context);
        if (context.equals("course")){
            String courseString = getIntent().getExtras().getString(context);
            Log.d(TAG, courseString);
            Course course = (Course) mParserService.parseObject(courseString, Course.class);
            TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
            mCourseNameTitle.setText(course.getName());
            List<Review> reviews = course.getReviews();
            Log.d(TAG, "num reviews total "+course.getTotalReviews()); //rétt tala
            Log.d(TAG, "num reviews "+reviews.size()); //0 for some reason
            addReviews(reviews, context);
        }
        else{
            String userString = getIntent().getExtras().getString(context);
            Log.d(TAG, userString);
            User user = (User) mParserService.parseObject(userString, User.class);
            TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
            mCourseNameTitle.setText(user.getUserName());
            List<Review> reviews = user.getReviews();
            addReviews(reviews, context);
        }

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();
    }

    private void addReviews(List<Review> reviews, String context){
        LinearLayout allReviews = findViewById(R.id.all_Reviews);
        for (Review review:reviews) {
            Log.d(TAG, review.toString());
            LayoutInflater inflater = LayoutInflater.from(ReadReviewsActivity.this);
            View reviewView = inflater.inflate(R.layout.review_layout, null);

            TextView comment = reviewView.findViewById(R.id.comment_text_view);
            comment.setText(review.getComment());
            ImageButton upvote = reviewView.findViewById(R.id.upvote_button);
            ImageButton downvote = reviewView.findViewById(R.id.downvote_button);

            TextView title = reviewView.findViewById(R.id.review_title);
            if (context.equals("course")){
                title.setText(review.getUserName());
                upvote.setOnClickListener(v -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
                    String userString = sharedPreferences.getString("loggedInUser", "");
                    JSONObject jsonBody = new JSONObject();
                    if (!userString.equals("")){
                        User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
                        try {
                            jsonBody.put("user", mParserService.deParseObject(loggedInUser));
                            jsonBody.put("review", mParserService.deParseObject(review));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addUpvoteDownvote(jsonBody,"/upvote/");
                    }

                });
                downvote.setOnClickListener(v -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
                    String userString = sharedPreferences.getString("loggedInUser", "");
                    JSONObject jsonBody = new JSONObject();
                    if (!userString.equals("")){
                        User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
                        try {
                            jsonBody.put("user", mParserService.deParseObject(loggedInUser));
                            jsonBody.put("review", mParserService.deParseObject(review));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addUpvoteDownvote(jsonBody,"/downvote/");
                    }
                });
            }
            else {
                title.setText(review.getCourseName());
                downvote.setVisibility(View.INVISIBLE);
                upvote.setVisibility(View.INVISIBLE);
            }
            allReviews.addView(reviewView);
        }
    }

    public void addUpvoteDownvote(JSONObject jsonBody,String requestUrl){
        mReviewService.semiGenericPOST(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                    }

                    @Override
                    public void onSuccess(String result) {

                        Log.d("TAG","upvote/downvote added");
                    }
                },jsonBody, requestUrl
        );
    }

}
