package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import is.hi.hbv601g.reithi_android.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash_screen);

        logoImageView = findViewById(R.id.logoImageView);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        logoImageView.startAnimation(fadeIn);

        // Add any additional code you want to run during the splash screen

        // Set a delay of 3 seconds before starting the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, LandingPageActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000); // 3000 milliseconds = 3 seconds
    }
}
