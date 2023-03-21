package is.hi.hbv601g.reithi_android.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;
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


public class ReviewPageActivity extends AppCompatActivity {

    private static final String TAG = "ReviewPageActivity";
    private NetworkManager mNetworkManager;
    private ReviewService mReviewService;
    private Button mSubmitReviewButton;
    private CourseService mCourseService;

    private UserService mUserService;
    private int[] mSliderValues;
    private ParserService mParserService;
    private EditText mCommentField;
    private String mUser;
    private List<SeekBar> mSeekBars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        mNetworkManager = NetworkManager.getInstance(this);
        mCourseService = new CourseService(this);
        mReviewService = new ReviewService(this);
        mUserService = new UserService(this);
        mParserService = ParserService.getInstance();

        mSubmitReviewButton = findViewById(R.id.submit_review_button);
        mCommentField = findViewById(R.id.comment);

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();

        mSeekBars = new ArrayList<>();
        FrameLayout[] sliders = {
                findViewById(R.id.slider_1),
                findViewById(R.id.slider_2),
                findViewById(R.id.slider_3),
                findViewById(R.id.slider_4),
                findViewById(R.id.slider_5)};

        mSliderValues = new int[]{1, 1, 1, 1, 1};

        mSubmitReviewButton.setOnClickListener(v -> {
            addReview();
        });

        for (FrameLayout progress : sliders) {
            SeekBar slider = progress.findViewById(R.id.slider);
            LinearLayout dots = progress.findViewById(R.id.dots);
            mSeekBars.add(slider);
            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Haptic feedback
                    if (vibrator.hasVibrator()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // For API 26+
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            // For API below 26
                            vibrator.vibrate(50);
                        }
                    }
                    //iterates through slider dots and colors them accordingly
                    for (int i = 1; i <= 5; i++) {
                        String name = "dot_" + i;
                        int id = getResources().getIdentifier(name, "id", getPackageName());
                        ImageView dotView = dots.findViewById(id);
                        if (progress >= 0) {
                            dotView.setImageResource(R.drawable.slider_dot_selected);
                        } else {
                            dotView.setImageResource(R.drawable.slider_dot_empty);
                        }
                        progress--;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Do something when the user starts touching the seek bar
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Do something when the user stops touching the seek bar
                }
            });

        }


    }

    private void getValuesfromSliders(){
        for (int i = 0; i<mSeekBars.size(); i++){
            mSliderValues[i] = mSeekBars.get(i).getProgress()+1;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Eitthvað kúl
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void addReview() {
        getValuesfromSliders();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("overallScore", mSliderValues[0]);
            jsonBody.put("difficulty", mSliderValues[1]);
            jsonBody.put("workload", mSliderValues[2]);
            jsonBody.put("teachingQuality", mSliderValues[3]);
            jsonBody.put("courseMaterial", mSliderValues[4]);
            jsonBody.put("comment", mCommentField.getText().toString());
            jsonBody.put("selectedCourse", getIntent().getExtras().getString("course"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        String userString = sharedPreferences.getString("loggedInUser", "");
        Log.d(TAG, userString);
        if (userString != ""){
            User loggedInUser = (User) (Object) mParserService.parseObject(userString, User.class);
            try {
                jsonBody.put("user", userString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mReviewService.addReviewPOST(new NetworkCallback<String>() {
                @Override
                public void onFailure(String errorString) {
                    Log.e(TAG, errorString);
                }

                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Review added");

                    // Show popup message
                    Toast.makeText(ReviewPageActivity.this, "Review successfully added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, jsonBody, "/addreview");

        }
    }


}