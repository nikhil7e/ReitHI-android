package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private Button mSignupButton;
    private TextView mUsernameTextView;
    private TextView mFacultyTextView;
    private ImageButton mEditFacultyButton;
    private Spinner mFacultySpinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_account);

        mParserService = ParserService.getInstance();


        mUsernameTextView = findViewById(R.id.username_text_view);
        mFacultyTextView = findViewById(R.id.faculty_text_view);
        mEditFacultyButton = findViewById(R.id.edit_faculty_button);
        mFacultySpinner = findViewById(R.id.faculty_spinner);

        // Set the text for the username and faculty TextViews
        //mUsernameTextView.setText("John Doe");
        //mFacultyTextView.setText("Engineering");

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

}
