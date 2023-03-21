package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Entities.Review;
import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.Fragments.BottomBarFragment;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;
import is.hi.hbv601g.reithi_android.Services.UserService;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private Button mSignupButton;
    private Button mLoginButton;
    private UserService mUserService;
    private EditText mUserNameInput;
    private EditText mPasswordInput;

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mParserService = ParserService.getInstance();
        mUserService = new UserService(this);


        mSignupButton = findViewById(R.id.signup_button);
        mSignupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(v -> {
            loginUser();
        });
        // Add the BottomAppBarFragment to the layout
        BottomBarFragment bottomAppBarFragment = new BottomBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bottomBar_fragment_container_view, bottomAppBarFragment);
        transaction.commit();
        mUserNameInput = findViewById(R.id.userName_input);
        mPasswordInput = findViewById(R.id.password_input);
        mSharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
    }

    private void loginUser(){
        JSONObject userBody = new JSONObject();
        try {
            userBody.put("username", mUserNameInput.getText().toString());
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
                /*Type listType = new TypeToken<List<User>>() {}.getType();
                List<User> userList = (List<User>) (Object) mParserService.parse(result, listType);
                User user = userList.get(0);*/
                User user = (User) (Object) mParserService.parseObject(result, User.class);
                Log.d(TAG, "input password: '" + mPasswordInput.getText() + "' correct password: '" + user.getPassword() + "'");
                if (user.getPassword().equals(mPasswordInput.getText().toString())){
                    Log.d(TAG, "result er " + result);
                    String userJson = mParserService.deParseObject(user);
                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("user", userJson);
                        mUserService.genericUserPOST(new NetworkCallback<String>() {
                            @Override
                            public void onFailure(String errorString) {
                                Log.e(TAG, errorString);
                                String userString = mSharedPreferences.getString("loggedInUser", "");
                                Log.d(TAG, userString);
                                if (userString.equals("")){
                                    Toast.makeText(LoginActivity.this, "Username or password wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onSuccess(String userString) {
                                Log.d(TAG, "Login Successful with user " + userString);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putString("loggedInUser", userString);
                                editor.apply();
                                Toast.makeText(LoginActivity.this, "Successfully logged in as " + user.getUserName(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, requestBody, "/login");
                    }catch (JSONException e){
                        //do something
                    }
                }
                else{
                    Log.d(TAG, "Password wrong");
                    Toast.makeText(LoginActivity.this, "Username or password wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, userBody, "/finduser");
    }

}
