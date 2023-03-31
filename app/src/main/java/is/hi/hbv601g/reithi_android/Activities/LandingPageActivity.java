package is.hi.hbv601g.reithi_android.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.Fragments.FilterFragment;
import is.hi.hbv601g.reithi_android.Fragments.SearchResultFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;

import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

//test push Eddi
//test push Tómas

public class LandingPageActivity extends AppCompatActivity {

    private static final String TAG = "LandingPageActivity";
    public static final String CHANNEL_ID = "1234";
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel_id", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My Channel Description");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

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
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                Log.d(TAG, "enter pressed" + event);
                performSearch();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
*/      if (mSearchResultFragment == null){
            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            LinearLayout loadingView = findViewById(R.id.loadingview);
            loadingView.addView(progressBar);

            mCourseService.semiGenericPOST(
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
                            }
                            loadingView.removeAllViews();
                            Log.d("TAG","Fragment added");
                        }
                    },getFilter(), "/filter/?name="+mSearchBar.getText().toString()+"&page=1"
            );
        }
        else{
            mSearchResultFragment.updateSearchQuery(mSearchBar.getText().toString());
            mSearchResultFragment.updateFromFilter(getFilter());
        }
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