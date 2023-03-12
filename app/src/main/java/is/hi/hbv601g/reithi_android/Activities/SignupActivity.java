package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private Button mBackToLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mParserService = ParserService.getInstance();


        mBackToLoginButton = findViewById(R.id.login_redirect_button);
        mBackToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }

}
