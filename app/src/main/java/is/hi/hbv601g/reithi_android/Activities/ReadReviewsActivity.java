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
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String courseString = getIntent().getExtras().getString("course");
        Log.d(TAG, courseString);

        LinearLayout allReviews = findViewById(R.id.all_Reviews);

        Course course = (Course) mParserService.parseObject(courseString, Course.class);


        TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
        mCourseNameTitle.setText(course.getName());


        List<Review> reviews = course.getReviews();
        Log.d(TAG, "num reviews total "+course.getTotalReviews()); //rétt tala
        Log.d(TAG, "num reviews "+reviews.size()); //0 for some reason
        for (Review review:reviews) {
            Log.d(TAG, review.toString());
            LayoutInflater inflater = LayoutInflater.from(ReadReviewsActivity.this);
            View reviewView = inflater.inflate(R.layout.review_layout, null);

            mUserService.genericUserGET(
                    new NetworkCallback<String>() {
                @Override
                public void onFailure(String errorString) {
                    Log.e(TAG, errorString);
                }

                @Override
                public void onSuccess(String result) {

                    User reviewUser = (User) (Object) mParserService.parseObject(result, User.class);
                    mCourseService.searchCoursesGET(
                            new NetworkCallback<String>() {
                                @Override
                                public void onFailure(String errorString) {
                                    Log.e(TAG, errorString);
                                }

                                @Override
                                public void onSuccess(String result) {

                                    Course reviewCourse = (Course) (Object) mParserService.parseObject(result, Course.class);
                                    TextView title = reviewView.findViewById(R.id.review_title);
                                    title.setText(reviewUser.getUserName());
                                    /*User user = review.getUser();
                                    Log.d(TAG, "Username is" + user.toString());*/
                                    /*
                                    title.setText(user.getUserName());*/
                                    TextView comment = reviewView.findViewById(R.id.comment_text_view);
                                    comment.setText(review.getComment());
                                    ImageButton upvote = reviewView.findViewById(R.id.upvote_button);
                                    ImageButton downvote = reviewView.findViewById(R.id.downvote_button);


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
                                            addUpvoteDownvote(jsonBody,"/upvote/", reviewUser.getDeviceToken());
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
                                            addUpvoteDownvote(jsonBody,"/downvote/", reviewUser.getDeviceToken());
                                        }
                                    });
                                    allReviews.addView(reviewView);
                                    Log.d("TAG","upvote/downvote added");
                                }
                            }, "/getcoursebyid/?id="+review.getCourseID());

                }
            }, "/getuserbyid/?id="+review.getUserID());
        }

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();

    }

    public void addUpvoteDownvote(JSONObject jsonBody,String requestUrl, String token){
        mReviewService.semiGenericPOST(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Map<String, String> data = new HashMap<>();

//                        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
//                                .setMessageId(Integer.toString(1234))
//                                .setData(data)
//                                .setNotification(new NotificationCompat.Builder(getApplicationContext(), "channel_id")
//                                        .setSmallIcon(R.drawable.user_icon)
//                                        .setContentTitle("A user has voted for your comment on ReitHÍ!")
//                                        .setContentText("Message")
//                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                                        .build())
//                                .build());

                        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("281319629505@fcm.googleapis.com")
                                .setMessageId(Integer.toString(1234))
                                .addData("receiver_token", token)
                                .addData("title", "A user has voted for your comment on ReitHÍ!")
                                .addData("body", "Message")
                                .addData("message", "Notification!!!")
                                .setTtl(100)
                                .build());


                        Log.d("TAG","upvote/downvote added");
                    }
                },jsonBody, requestUrl
        );
    }

}
