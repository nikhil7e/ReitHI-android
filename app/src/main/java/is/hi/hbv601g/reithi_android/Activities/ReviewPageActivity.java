package is.hi.hbv601g.reithi_android.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Fragments.SearchResultFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;

//test push Eddi
//test push Tómas

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

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Eitthvað kúl
    }

//    private FragmentTransaction getSearchResultFragmentTransaction() {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        return fragmentTransaction;
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}