package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private ParserService mParserService;
    private UserService mUserService;

    private NetworkManager mNetworkManager;

    private Button mBackToLoginButton;
    private Button mSignupButton;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordVerifyField;
    private SharedPreferences mSharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mParserService = ParserService.getInstance();
        mUserService = new UserService(this);
        mSharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);

        mBackToLoginButton = findViewById(R.id.login_redirect_button);
        mBackToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        mUsernameField = findViewById(R.id.userName_input);
        mPasswordField = findViewById(R.id.password_input);
        mPasswordVerifyField = findViewById(R.id.password_verify_input);
        mSignupButton = findViewById(R.id.signup_button);

        mSignupButton.setOnClickListener(v -> {
            if (!mPasswordField.getText().toString().equals(mPasswordVerifyField.getText().toString())) {
                Toast.makeText(SignupActivity.this, "Please ensure the passwords match", Toast.LENGTH_SHORT).show();
            } else {
                signupUser();
            }
        });

        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();

    }

    private void signupUser() {
        JSONObject userBody = new JSONObject();
        try {
            User user = new User(mUsernameField.getText().toString(), mPasswordField.getText().toString());
            userBody.put("user", mParserService.deParseObject(user));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mUserService.genericUserPOST(new NetworkCallback<String>() {
            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, errorString);
                Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
//                String userString = mSharedPreferences.getString("loggedInUser", "");
//                Log.d(TAG, userString);
//                if (userString.equals("")) {
//                    Toast.makeText(SignupActivity.this, "Username or password wrong", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onSuccess(String userString) {
                Log.d(TAG, "Signup Successful with user " + userString);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("loggedInUser", userString);
                editor.apply();
                Toast.makeText(SignupActivity.this, "Successfully signed up as " + mUsernameField.getText(), Toast.LENGTH_SHORT).show();
                setResult(200);
                finish();
            }
        }, userBody, "/signup");
    }

}
