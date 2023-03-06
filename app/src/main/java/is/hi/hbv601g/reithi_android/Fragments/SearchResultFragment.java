package is.hi.hbv601g.reithi_android.Fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Activities.CourseActivity;
import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class SearchResultFragment extends Fragment {

    private ParserService mParserService;

    private final String TAG = "SearchResultFragment";
    private LinearLayout mSearchResults;

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        /** Inflating the layout for this fragment **/
//        View v = inflater.inflate(R.layout.fragment_search_result, null);
//        return v;
//    }
    public SearchResultFragment() {
        super(R.layout.fragment_search_result);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mParserService = ParserService.getInstance();
        mSearchResults = view.findViewById(R.id.search_results);
        Type listType = new TypeToken<List<Course>>() {

        }.getType();
        String results = requireArguments().getString("searchResult");
        List<Course> courseList = (List<Course>)(Object) mParserService.parse(results, listType);
        Context context = getActivity();
        if (courseList.size() == 0) {
            TextView noCourses = new TextView(context);
            noCourses.setText("No Courses Found!");
            mSearchResults.addView(noCourses);
        }

        for (Course course : courseList) {
            LinearLayout searchResultLayout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            searchResultLayout.setLayoutParams(layoutParams);
            TextView searchResultTextView = new TextView(context);
            searchResultTextView.setText((course.getCredits()).toString() + " credits");
            searchResultLayout.addView(searchResultTextView);
            Button searchResultButton = new Button(context);
            searchResultButton.setText(course.getName());
            searchResultButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Object> courseList = new ArrayList<>();
                    courseList.add(course);
                    Intent intent = new Intent(context, CourseActivity.class);
                    intent.putExtra("course", mParserService.deParse(courseList));
                    context.startActivity(intent);
                }
            });
            searchResultLayout.addView(searchResultButton);
            mSearchResults.addView(searchResultLayout);
        }

    }

}
