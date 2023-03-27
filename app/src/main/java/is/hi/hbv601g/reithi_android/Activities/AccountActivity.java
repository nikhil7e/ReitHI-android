package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;
import is.hi.hbv601g.reithi_android.Services.UserService;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private Button mLogoutButton;
    private TextView mUsernameTextView;
    private TextView mFacultyTextView;
    private ImageButton mEditFacultyButton;
    private Spinner mFacultySpinner;

    private boolean isDarkMode = false;
    private ToggleButton mToggleDayNight;
    private TextView mToggleDayNightText;

    private Button mReadReviewsButton;
    private UserService mUserService;
    private User mLoggedInUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_account);

        mParserService = ParserService.getInstance();
        mUserService = new UserService(this);
        mUsernameTextView = findViewById(R.id.username_text_view);
        mFacultyTextView = findViewById(R.id.faculty_text_view);
        mEditFacultyButton = findViewById(R.id.edit_faculty_button);
        mFacultySpinner = findViewById(R.id.faculty_spinner);
        mToggleDayNight = findViewById(R.id.toggle_daynight);
        mToggleDayNightText = findViewById(R.id.toggle_daynight_text);
        mLogoutButton = findViewById(R.id.logout_button);
        mReadReviewsButton = findViewById(R.id.reviews_button);
        mToggleDayNight.setOnClickListener(v -> {

            if (mToggleDayNight.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //mToggleDayNightText.setText("Switch to day mode");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //mToggleDayNightText.setText("Switch to night mode");
            }
            recreate();

        });

        mReadReviewsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ReadReviewsActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
            String userString = mParserService.deParseObject(mLoggedInUser);
            if (userString != ""){
                intent.putExtra("user", userString);
                intent.putExtra("context", "user");
                startActivity(intent);
            }
        });

        mLogoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loggedInUser", "");
            editor.apply();
            finish();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();
        });

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();

        // Set up the spinner with the faculty list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.faculty_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFacultySpinner.setAdapter(adapter);
        // Set the spinner item select listener
        mFacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from the spinner
                String selectedText = parent.getItemAtPosition(position).toString();

                // Set the text of mFacultyTextView
                mFacultyTextView.setText(selectedText);

                // Close the spinner
                mFacultySpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    public void onEditFacultyButtonClick(View view) {
        // Show/hide the spinner when the edit button is clicked
        if (mFacultySpinner.getVisibility() == View.GONE) {
            mFacultySpinner.setVisibility(View.VISIBLE);
        } else {
            mFacultySpinner.setVisibility(View.GONE);
        }
    }

    protected void onResume() {
        super.onResume();
        fillAccountInfo();
    }

    public void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        recreate(); // Recreate the activity to apply the new theme
    }

    private void fillAccountInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        String userString = sharedPreferences.getString("loggedInUser", "");
        Log.d(TAG, userString);
        if (userString != ""){
            User tempUser = (User) (Object) mParserService.parseObject(userString, User.class);
            mUsernameTextView.setText(tempUser.getUserName());
            reloadUser(tempUser);
        }
    }

    private void reloadUser(User user){
        JSONObject userBody = new JSONObject();
        try {
            userBody.put("username", user.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mUserService.genericUserPOST(new NetworkCallback<String>() {
            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, errorString);
            }

            @Override
            public void onSuccess(String result) {
                mLoggedInUser = (User) (Object) mParserService.parseObject(result, User.class);
            }
        }, userBody, "/finduser");
    }

}
