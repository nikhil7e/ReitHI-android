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

    private SearchResultFragment mSearchResultFragment = null;
    private FilterFragment mFilterFragment;

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

        mFilterFragment = new FilterFragment ();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.add(R.id.navigation_drawer, mFilterFragment);
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
      /*  JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", mSearchBar.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        mCourseService.filterPOST(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                    }

                    @Override
                    public void onSuccess(String result) {
                        mCourseSearchResults = result;
                        Log.d(TAG, result);
                        if (mSearchResultFragment == null){
                            mSearchResultFragment = new SearchResultFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("searchResult", result);
                            bundle.putString("searchQuery", mSearchBar.getText().toString());
                            mSearchResultFragment.setArguments(bundle);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.search_results_fragment_container_view, mSearchResultFragment);
                            transaction.commit();
                        }else{
                            mSearchResultFragment.updateSearchQuery(mSearchBar.getText().toString());
                            mSearchResultFragment.updateFromFilter(getFilter());
                        }
                        Log.d("TAG","Fragment added");
                    }
                },getFilter(), "/filter/?name="+mSearchBar.getText().toString()
        );
    }


    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    public void forwardFilter(JSONObject filtered){
        mSearchResultFragment.updateFromFilter(filtered);
    }

    public JSONObject getFilter(){
        Log.d(TAG, "CALLED GET FILTER");
        return mFilterFragment.getFilterObject();
    }
}