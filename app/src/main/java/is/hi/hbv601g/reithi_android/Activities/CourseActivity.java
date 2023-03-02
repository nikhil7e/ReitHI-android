package is.hi.hbv601g.reithi_android.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "coursePageActivity";

    private NetworkManager mNetworkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
    }
}
