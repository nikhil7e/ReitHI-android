package is.hi.hbv601g.reithi_android.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.Fragments.FilterFragment;
import is.hi.hbv601g.reithi_android.Fragments.SearchResultFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

//test push Eddi
//test push Tómas

public class LandingPageActivity extends AppCompatActivity {

    private static final String TAG = "LandingPageActivity";
    private NetworkManager mNetworkManager;

    private EditText mSearchBar;
    private ImageButton mSearchButton;
    private Button mFilterButton;
    private LinearLayout mCourseSearchResultsText;
    private String mCourseSearchResults;
    private CourseService mCourseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        mCourseService = new CourseService(this);
        mNetworkManager = NetworkManager.getInstance(this);
        mSearchBar = findViewById(R.id.search_bar);
        mSearchButton = findViewById(R.id.search_button);

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();

        FilterFragment filterFragment = new FilterFragment ();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.add(R.id.navigation_drawer, filterFragment);
        transaction2.commit();


        mSearchButton.setOnClickListener(
                view -> {
                    performSearch();
                }
        );

        mSearchBar.setOnEditorActionListener((v, actionId, event) -> {
            Log.d(TAG, "enter pressed");
            performSearch();
            return true;
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

    private void performSearch() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", mSearchBar.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                }, jsonBody, "/searchcourses");
    }


    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }
}