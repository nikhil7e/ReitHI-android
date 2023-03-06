package is.hi.hbv601g.reithi_android.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "coursePageActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;

    private Button mSearchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mSearchButton = findViewById(R.id.review_button);
        setContentView(R.layout.activity_course);

        mParserService = ParserService.getInstance();
        String course = getIntent().getExtras().getString("course");
        Log.d(TAG, course);
    }
    // TODO : Setja inn event listiner fyrir review
//    searchResultButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            List<Object> courseList = new ArrayList<>();
//            courseList.add(course);
//            Intent intent = new Intent(context, CourseActivity.class);
//            intent.putExtra("course", mParserService.deParse(courseList));
//            context.startActivity(intent);
//        }
//    });

//    @Override
//    public void onCreate( Bundle savedInstanceState) {
//        mParserService = ParserService.getInstance();
//        mSearchResults = view.findViewById(R.id.search_results);
//        Type courseType = new TypeToken<Course>() { }.getType();
//        String results = requireArguments().getString("searchResult");
//        List<Course> courseList = (List<Course>)(Object) mParserService.parse(results, listType);
//        Context context = getActivity();
//        if (courseList.size() == 0) {
//            TextView noCourses = new TextView(context);
//            noCourses.setText("No Courses Found!");
//            mSearchResults.addView(noCourses);
//        }
//
//        for (Course course : courseList) {
//            LinearLayout searchResultLayout = new LinearLayout(context);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            searchResultLayout.setLayoutParams(layoutParams);
//            TextView searchResultTextView = new TextView(context);
//            searchResultTextView.setText((course.getCredits()).toString() + " credits");
//            searchResultLayout.addView(searchResultTextView);
//            Button searchResultButton = new Button(context);
//            searchResultButton.setText(course.getName());
//            searchResultButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, CourseActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//            searchResultLayout.addView(searchResultButton);
//            mSearchResults.addView(searchResultLayout);
//        }
//
//    }
}
