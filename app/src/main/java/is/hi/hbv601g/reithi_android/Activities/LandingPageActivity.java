package is.hi.hbv601g.reithi_android.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;

//test push Eddi
//test push Tómas

public class LandingPageActivity extends AppCompatActivity {

    private static final String TAG = "LandingPageActivity";
    private NetworkManager mNetworkManager;

    private EditText mSearchBar;
    private Button mSearchButton;
    private TextView mCourseSearchResultsText;
    private List<Course> mCourseSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mNetworkManager = NetworkManager.getInstance(this);
        mSearchBar = findViewById(R.id.search_bar);
        mSearchButton = findViewById(R.id.search_button);
        mCourseSearchResultsText = findViewById(R.id.course_results_text);

        mSearchButton.setOnClickListener(
                view -> {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", mSearchBar.getText().toString());

                    Log.d(TAG, params.get("name"));

                    mNetworkManager.searchCoursesPOST(
                            new NetworkCallback<List<Course>>() {
                                @Override
                                public void onFailure(String errorString) {
                                    Log.e(TAG, errorString);
                                }

                                @Override
                                public void onSuccess(List<Course> result) {
                                    mCourseSearchResultsList = result;
                                    for (Course r : mCourseSearchResultsList) {
                                        mCourseSearchResultsText.setText(mCourseSearchResultsText.getText() + "\n" + r.getName());
                                    }
                                }
                            }, params);
                });

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