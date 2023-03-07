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
import androidx.fragment.app.FragmentManager;

import com.google.android.material.slider.Slider;

import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Fragments.SearchResultFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;


public class ReviewPageActivity extends AppCompatActivity {

    private static final String TAG = "ReviewPageActivity";
    private NetworkManager mNetworkManager;
    private Button mSubmitReviewButton;
    private CourseService mCourseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        mNetworkManager = NetworkManager.getInstance(this);
        mCourseService = new CourseService(this);

        mSubmitReviewButton = findViewById(R.id.submit_review_button);
        FrameLayout[] sliders = {
                findViewById(R.id.slider_1),
                findViewById(R.id.slider_2),
                findViewById(R.id.slider_3),
                findViewById(R.id.slider_4),
                findViewById(R.id.slider_5)};

        for (FrameLayout progress: sliders) {
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
                    //iterates through slider dots and colors them accordingly
                    for (int i = 1; i <= 5; i++){
                        String name = "dot_" + i;
                        int id = getResources().getIdentifier(name, "id", getPackageName());
                        ImageView dotView = dots.findViewById(id);
                        if (currentValue>=0){
                            dotView.setImageResource(R.drawable.slider_dot_selected);
                        }
                        else{
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

}