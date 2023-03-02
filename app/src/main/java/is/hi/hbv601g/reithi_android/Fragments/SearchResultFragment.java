package is.hi.hbv601g.reithi_android.Fragments;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class SearchResultFragment extends Fragment {

    private ParserService mParserService;

    private final String TAG = "SearchResultFragment";
    private TextView mSearchResult;
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
        mSearchResult = view.findViewById(R.id.search_result);
        Type listType = new TypeToken<List<Course>>() {

        }.getType();
        String results = requireArguments().getString("searchResult");
        // int someInt = requireArguments().getInt("some_int");
        List<Course> courseList = (List<Course>)(Object) mParserService.parse(results, listType);
        if (courseList.size() > 0) {
            mSearchResult.setText("");
        }
        for (Course course : courseList) {
            mSearchResult.append(course.getName() + "\n");
        }
    }

}
