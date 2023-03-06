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


        mSubmitReviewButton.setOnClickListener(
                view -> {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", mSearchBar.getText().toString());
                    Log.d(TAG, params.get("name"));

                    mCourseService.searchCoursesPOST(
                            new NetworkCallback<String>() {
                                @Override
                                public void onFailure(String errorString) {
                                    Log.e(TAG, errorString);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    mCourseSearchResults = result;
                                    androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
                                    Fragment fragment = fm.findFragmentById(R.id.search_results_fragment_container_view);
                                    Bundle bundle = new Bundle();
                                    // bundle.putSerializable("searchResult", result);
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
                                    Log.d("TAG","Fragment added");
                                }
                            }, params, "/searchcourses");
                });
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