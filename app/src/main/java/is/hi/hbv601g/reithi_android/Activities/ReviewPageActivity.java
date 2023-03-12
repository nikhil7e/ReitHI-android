package is.hi.hbv601g.reithi_android.Activities;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.Fragments.SearchResultFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;
import is.hi.hbv601g.reithi_android.Services.ParserService;
import is.hi.hbv601g.reithi_android.Services.ReviewService;


public class ReviewPageActivity extends AppCompatActivity {

    private static final String TAG = "ReviewPageActivity";
    private NetworkManager mNetworkManager;
    private ReviewService mReviewService;
    private Button mSubmitReviewButton;
    private CourseService mCourseService;
    private int[] mSliderValues;
    private ParserService mParserService;
    private EditText mCommentField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        mNetworkManager = NetworkManager.getInstance(this);
        mCourseService = new CourseService(this);
        mReviewService = new ReviewService(this);
        mParserService = ParserService.getInstance();

        mSubmitReviewButton = findViewById(R.id.submit_review_button);
        mCommentField = findViewById(R.id.comment);

        FrameLayout[] sliders = {
                findViewById(R.id.slider_1),
                findViewById(R.id.slider_2),
                findViewById(R.id.slider_3),
                findViewById(R.id.slider_4),
                findViewById(R.id.slider_5)};

        mSliderValues = new int[]{1, 1, 1, 1, 1};

        mSubmitReviewButton.setOnClickListener(v -> {
//            Intent intent = new Intent(CourseActivity.this, ReviewPageActivity.class);
//            intent.putExtra("course", courseString);
//            startActivity(intent);
            addReview();
        });

        for (FrameLayout progress : sliders) {
            SeekBar slider = progress.findViewById(R.id.slider);
            LinearLayout dots = progress.findViewById(R.id.dots);
            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    // Haptic feedback
                    if (vibrator.hasVibrator()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // For API 26+
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            // For API below 26
                            vibrator.vibrate(500);
                        }
                    }
                    int currentValue = seekBar.getProgress();

                    switch (progress) {
                        case 0: {
                            mSliderValues[0] = currentValue;
                        }
                        case 1: {
                            mSliderValues[1] = currentValue;
                        }
                        case 2: {
                            mSliderValues[2] = currentValue;
                        }
                        case 3: {
                            mSliderValues[3] = currentValue;
                        }
                        case 4: {
                            mSliderValues[4] = currentValue;
                        }
                    }

                    for (int x : mSliderValues) {
                        Log.d(TAG, "Slidervalues eru " + x);
                    }

                    //iterates through slider dots and colors them accordingly
                    for (int i = 1; i <= 5; i++) {
                        String name = "dot_" + i;
                        int id = getResources().getIdentifier(name, "id", getPackageName());
                        ImageView dotView = dots.findViewById(id);
                        if (currentValue >= 0) {
                            dotView.setImageResource(R.drawable.slider_dot_selected);
                        } else {
                            dotView.setImageResource(R.drawable.slider_dot_empty);
                        }
                        currentValue--;
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
        Map<String, String> params = new HashMap<>();

        params.put("overallScore", String.valueOf(mSliderValues[0]));
        params.put("difficulty", String.valueOf(mSliderValues[1]));
        params.put("workload", String.valueOf(mSliderValues[2]));
        params.put("teachingQuality", String.valueOf(mSliderValues[3]));
        params.put("courseMaterial", String.valueOf(mSliderValues[4]));

        params.put("comment", mCommentField.getText().toString());

        User u = new User("x", "x");
        List<Object> ul = new ArrayList<>();
        ul.add(u);
        params.put("user", mParserService.deParse(ul));

        params.put("selectedCourse", getIntent().getExtras().getString("course"));


        mReviewService.addReviewPOST(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                    }

                    @Override
                    public void onSuccess(String result) {
                        androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
                        Fragment fragment = fm.findFragmentById(R.id.search_results_fragment_container_view);
                        Bundle bundle = new Bundle();
                        bundle.putString("searchResult", result);
                        if (fragment == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.search_results_fragment_container_view, SearchResultFragment.class, bundle)
                                    .commit();
                        } else {
                            fragment.getFragmentManager().beginTransaction().detach(fragment).commit();
                            fragment.setArguments(bundle);
                            fragment.getFragmentManager().beginTransaction().attach(fragment).commit();
                        }
                        Log.d("TAG", "Fragment added");
                    }
                }, params, "/addreview");
    }

}