package is.hi.hbv601g.reithi_android.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.function.Consumer;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Entities.Review;
import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
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

    private SharedPreferences mSharedPreferences;

    private User mLoggedInUser;

    private String mContext;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_reviews);

        mParserService = ParserService.getInstance();
        mReviewService = new ReviewService(this);
        mUserService = new UserService(this);
        mCourseService = new CourseService(this);
        mSharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        mContext = getIntent().getExtras().getString("context");
        Log.d(TAG, mContext);


        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();
    }

    protected void onResume() {
        super.onResume();
        if (mContext.equals("course")) {
            String courseString = getIntent().getExtras().getString(mContext);
            String userString = mSharedPreferences.getString("loggedInUser", "");
            if (!userString.equals("")){
                mLoggedInUser = (User) mParserService.parseObject(userString, User.class);
            }
            Log.d(TAG, courseString);
            Course course = (Course) mParserService.parseObject(courseString, Course.class);
            TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
            mCourseNameTitle.setText(course.getName());
            List<Review> reviews = course.getReviews();
            Log.d(TAG, "num reviews total " + course.getTotalReviews()); //rétt tala
            Log.d(TAG, "num reviews " + reviews.size()); //0 for some reason
            addReviews(reviews, mContext);
        } else {
            String userString = getIntent().getExtras().getString(mContext);
            Log.d(TAG, userString);
            if (userString.equals("null")){
                userString = mSharedPreferences.getString("loggedInUser", "");
            }
            mLoggedInUser = (User) mParserService.parseObject(userString, User.class);
            TextView mCourseNameTitle = findViewById(R.id.reviewCourseName);
            mCourseNameTitle.setText(mLoggedInUser.getUserName());
            List<Review> reviews = mLoggedInUser.getReviews();
            addReviews(reviews, mContext);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addReviews(List<Review> reviews, String context) {
        LinearLayout allReviews = findViewById(R.id.all_Reviews);
        Drawable upvoteFilled = ResourcesCompat.getDrawable(getResources(), R.drawable.upvote_icon_filled, getTheme());
        Drawable upvoteEmpty = ResourcesCompat.getDrawable(getResources(), R.drawable.upvote_icon, getTheme());
        Drawable downvoteFilled = ResourcesCompat.getDrawable(getResources(), R.drawable.downvote_icon_filled, getTheme());
        Drawable downvoteEmpty = ResourcesCompat.getDrawable(getResources(), R.drawable.downvote_icon, getTheme());

        for (Review review : reviews) {
            Log.d(TAG, review.toString());
            LayoutInflater inflater = LayoutInflater.from(ReadReviewsActivity.this);
            View reviewView = inflater.inflate(R.layout.review_layout, null);

            updateUpvotesDownvotes(reviewView.findViewById(R.id.upvotes_downvotes_text), Integer.parseInt(review.getUpvotes()));
            TextView comment = reviewView.findViewById(R.id.comment_text_view);
            comment.setText(review.getComment());
            ImageButton upvote = reviewView.findViewById(R.id.upvote_button);
            ImageButton downvote = reviewView.findViewById(R.id.downvote_button);
            ImageButton deleteButton = reviewView.findViewById(R.id.delete_review_button);
            addRatings(review, reviewView);
            for (String userId: review.getUpvoterIds()) {
                if (String.valueOf(mLoggedInUser.getID()).equals(userId)){
                    upvote.setImageDrawable(upvoteFilled);
                }
            }
            for (String userId: review.getDownvoterIds()) {
                if (String.valueOf(mLoggedInUser.getID()).equals(userId)){
                    downvote.setImageDrawable(downvoteFilled);
                }
            }
            if (mLoggedInUser.getID() != review.getUserID()){
                deleteButton.setVisibility(View.INVISIBLE);
            }
            deleteButton.setOnTouchListener((v, event) -> {
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
            });
            TextView title = reviewView.findViewById(R.id.review_title);
            String userGuard = mSharedPreferences.getString("loggedInUser", "");
            if (context.equals("course") && !userGuard.equals("")) {
                title.setText(review.getUserName());
                upvote.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Drawable currentDrawable = upvote.getDrawable();
                        if (upvote.getDrawable().getConstantState().equals(upvoteFilled.getConstantState())) {
                            upvote.setImageDrawable(upvoteEmpty);
                        } else {
                            upvote.setImageDrawable(upvoteFilled);
                        }
                        String userString = mSharedPreferences.getString("loggedInUser", "");
                        JSONObject jsonBody = new JSONObject();
                        if (!userString.equals("")) {
                            User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
                            try {
                                jsonBody.put("user", mParserService.deParseObject(loggedInUser));
                                jsonBody.put("review", mParserService.deParseObject(review));
                                jsonBody.put("deviceToken", mParserService.deParseObject(review.getUserToken()));
                                jsonBody.put("courseName", mParserService.deParseObject(review.getCourseName()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            addUpvoteDownvote(jsonBody, "/upvote/", review.getUserToken(), result -> {
                                // Do something with the result
                                updateUpvotesDownvotes(reviewView.findViewById(R.id.upvotes_downvotes_text), result);
                                System.out.println("Result: " + result);
                            });
                        }
                    }
                    return true;
                });
                downvote.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (downvote.getDrawable().getConstantState() == downvoteFilled.getConstantState()) {
                            downvote.setImageDrawable(downvoteEmpty);
                        } else {
                            downvote.setImageDrawable(downvoteFilled);
                        }
                        String userString = mSharedPreferences.getString("loggedInUser", "");
                        JSONObject jsonBody = new JSONObject();
                        if (!userString.equals("")) {
                            User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
                            try {
                                jsonBody.put("user", mParserService.deParseObject(loggedInUser));
                                jsonBody.put("review", mParserService.deParseObject(review));
                                jsonBody.put("deviceToken", mParserService.deParseObject(review.getUserToken()));
                                jsonBody.put("courseName", mParserService.deParseObject(review.getCourseName()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            addUpvoteDownvote(jsonBody, "/downvote/", review.getUserToken(), result -> {
                                // Do something with the result
                                updateUpvotesDownvotes(reviewView.findViewById(R.id.upvotes_downvotes_text), result);
                                System.out.println("Result: " + result);
                            });
                        }
                    }
                    return true;
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
                }
                callback.accept(Integer.parseInt(result));

            }
        }, jsonBody, requestUrl);
    }

    private void addRatings(Review review, View view){
        int[] ratings = {review.getOverallScore(), review.getDifficulty(), review.getCourseMaterial(), review.getWorkload(), review.getTeachingQuality()};
        String[] headings = {"Overall Score", "Difficulty", "Material", "Workload", "Teaching Quality"};
        LinearLayout textLayout = view.findViewById(R.id.rating_layout_text);
        LinearLayout ratingsLayout = view.findViewById(R.id.rating_layout_ratings);
        for (int j = 0; j < 5; j++) {

            LinearLayout ratingLayout = new LinearLayout(this);
            TextView ratingTextView = new TextView(this);
            ratingTextView.setText(headings[j]);
            textLayout.addView(ratingTextView);

            int rating = ratings[j];

            for (int i = 0; i < 5; i++) {
                Drawable full = ResourcesCompat.getDrawable(getResources(), R.drawable.ratingdot_full, this.getTheme());
                Drawable empty = ResourcesCompat.getDrawable(getResources(), R.drawable.ratingdot_empty, this.getTheme());
                ImageView circleView = new ImageView(this);
                if (rating >= 1) {
                    rating--;
                    circleView.setBackground(full);
                }  else {
                    circleView.setBackground(empty);
                }
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                        50,
                        50//ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(4, 0, 4, 0);

                circleView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                circleView.setPadding(3, 3, 3, 3);
                circleView.setLayoutParams(params);

                ratingLayout.addView(circleView);
            }
            ratingsLayout.addView(ratingLayout);
        }
    }

}
