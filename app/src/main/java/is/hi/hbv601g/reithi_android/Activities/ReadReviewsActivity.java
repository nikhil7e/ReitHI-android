package is.hi.hbv601g.reithi_android.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.messaging.Message;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

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
        if (context.equals("course")) {
            String courseString = getIntent().getExtras().getString(context);
            Log.d(TAG, courseString);
            Course course = (Course) mParserService.parseObject(courseString, Course.class);
            TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
            mCourseNameTitle.setText(course.getName());
            List<Review> reviews = course.getReviews();
            Log.d(TAG, "num reviews total " + course.getTotalReviews()); //rétt tala
            Log.d(TAG, "num reviews " + reviews.size()); //0 for some reason
            addReviews(reviews, context);
        } else {
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

    @SuppressLint("ClickableViewAccessibility")
    private void addReviews(List<Review> reviews, String context) {
        LinearLayout allReviews = findViewById(R.id.all_Reviews);
        for (Review review : reviews) {
            Log.d(TAG, review.toString());
            LayoutInflater inflater = LayoutInflater.from(ReadReviewsActivity.this);
            View reviewView = inflater.inflate(R.layout.review_layout, null);
            updateUpvotesDownvotes(reviewView.findViewById(R.id.upvotes_downvotes_text), review.upvoteCount());
            TextView comment = reviewView.findViewById(R.id.comment_text_view);
            comment.setText(review.getComment());
            ImageButton upvote = reviewView.findViewById(R.id.upvote_button);
            ImageButton downvote = reviewView.findViewById(R.id.downvote_button);
            ImageButton deleteButton = reviewView.findViewById(R.id.delete_review_button);
            deleteButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Change button icon to pressed state
                        deleteButton.setBackgroundResource(R.drawable.delete_icon_pressed);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Change button icon to default state
                        deleteButton.setBackgroundResource(R.drawable.delete_icon);

                        // Show confirmation dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReadReviewsActivity.this);
                        builder.setMessage("Are you sure you want to delete this review?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        JSONObject jsonBody = new JSONObject();
                                        try {
                                            jsonBody.put("review", mParserService.deParseObject(review));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mCourseService.semiGenericPOST(new NetworkCallback<String>() {
                                            @Override
                                            public void onFailure(String errorString) {
                                                Log.e(TAG, errorString);

                                            }

                                            @Override
                                            public void onSuccess(String result) {
                                                Log.d("TAG", "deleted review");
                                                allReviews.removeView(reviewView);
                                                Toast.makeText(ReadReviewsActivity.this, "Review successfully deleted", Toast.LENGTH_SHORT).show();

                                            }
                                        }, jsonBody, "/deletereview/");
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        builder.create().show();
                    }
                    return true;
                }
            });


            TextView title = reviewView.findViewById(R.id.review_title);
            if (context.equals("course")) {
                title.setText(review.getUserName());
                upvote.setOnClickListener(v -> {

                    SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
                    String userString = sharedPreferences.getString("loggedInUser", "");
                    JSONObject jsonBody = new JSONObject();
                    if (!userString.equals("")) {
                        User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
                        try {
                            jsonBody.put("user", mParserService.deParseObject(loggedInUser));
                            jsonBody.put("review", mParserService.deParseObject(review));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addUpvoteDownvote(jsonBody, "/upvote/", review.getUserToken(), result -> {
                            // Do something with the result
                            updateUpvotesDownvotes(reviewView.findViewById(R.id.upvotes_downvotes_text), result);
                            System.out.println("Result: " + result);
                        });
                    }

                });
                downvote.setOnClickListener(v -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
                    String userString = sharedPreferences.getString("loggedInUser", "");
                    JSONObject jsonBody = new JSONObject();
                    if (!userString.equals("")) {
                        User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
                        try {
                            jsonBody.put("user", mParserService.deParseObject(loggedInUser));
                            jsonBody.put("review", mParserService.deParseObject(review));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addUpvoteDownvote(jsonBody, "/downvote/", review.getUserToken(), result -> {
                            // Do something with the result
                            updateUpvotesDownvotes(reviewView.findViewById(R.id.upvotes_downvotes_text), result);
                            System.out.println("Result: " + result);
                        });
                    }
                });
            } else {
                title.setText(review.getCourseName());
                downvote.setVisibility(View.INVISIBLE);
                upvote.setVisibility(View.INVISIBLE);
            }
            allReviews.addView(reviewView);
        }
    }

    private void updateUpvotesDownvotes(TextView textView, Integer count) {
        textView.setText(String.valueOf(count));
    }

    public void addUpvoteDownvote(JSONObject jsonBody, String requestUrl, String token, Consumer<Integer> callback) {
        mReviewService.semiGenericPOST(new NetworkCallback<String>() {
            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, errorString);
                callback.accept(0);
            }

            @Override
            public void onSuccess(String result) {
                Log.d("TAG", "upvote/downvote added");
                if (!token.equals("")) {
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "1234")
//                            .setSmallIcon(R.drawable.user_icon)
//                            .setContentTitle("Test")
//                            .setContentText("testes")
//                            .setPriority(NotificationCompat.PRIORITY_MAX);
//
//                    // 281319629505
//                    RemoteMessage message = new RemoteMessage.Builder(token + "@fcm.googleapis.com")
//                            .setMessageId(System.currentTimeMillis() + token)
//                            .addData("title", "test")
//                            .addData("receiver_token", token)
//                            .addData("body", "test")
//                            .addData("channel_id", "1234")
//                            .build();
//
//                    FirebaseMessaging.getInstance().send(message);


//                    FirebaseMessaging.getInstance().send(
//                            new RemoteMessage.Builder("281319629505@fcm.googleapis.com")
//                                    .setMessageId(System.currentTimeMillis() + token)
//                                    .addData("key", "value")
//                                    .
//                                    .build());

                    // See documentation on defining a message payload.
                    Message message = Message. . Builder()
                            .putData("score", "850")
                            .putData("time", "2:45")
                            .setTopic(topic)
                            .build();

// Send a message to the devices subscribed to the provided topic.
                    String response = FirebaseMessaging.getInstance().send(message);


//                    FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("281319629505@fcm.googleapis.com")
//                            .setMessageId(Long.toString(System.currentTimeMillis()) + token)
//                            .addData("receiver_token", token)
//                            .addData("title", "A user has voted for your comment on ReitHÍ!")
//                            .addData("body", "Message")
//                            .addData("message", "Notification!!!")
//                            .setTtl(0)
//                            .build());
//
//                    FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("281319629505@fcm.googleapis.com")
//                            .setMessageId(Long.toString(System.currentTimeMillis()) + token)
//                            .addData("receiver_token", token)
//                            .addData("title", "A user has voted for your comment on ReitHÍ!")
//                            .addData("body", "Message")
//                            .addData("message", "Notification!!!")
//                            .setTtl(0)
//                            .build());
                }
                callback.accept(Integer.parseInt(result));

            }
        }, jsonBody, requestUrl);
    }

    public void sendToToken() throws Exception{
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.
        String registrationToken = "YOUR_REGISTRATION_TOKEN";

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
        // [END send_to_token]
    }

}
