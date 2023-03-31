package is.hi.hbv601g.reithi_android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

import is.hi.hbv601g.reithi_android.Activities.CourseActivity;
import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Entities.Page;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class SearchResultFragment extends Fragment {

    private ParserService mParserService;

    private final String TAG = "SearchResultFragment";
    private LinearLayout mSearchResults;
    
    private Context mContext;

    private Button mNextButton;
    private Button mPreviousButton;

    private CourseService mCourseService;

    private String mSearchQuery;

    private Page<Course> mCoursePage;
    private int mCurrentPage;

    private boolean coldStart;



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
        coldStart = true;
        mParserService = ParserService.getInstance();
        mSearchResults = view.findViewById(R.id.search_results);
        mPreviousButton = view.findViewById(R.id.prevButton);
        mNextButton = view.findViewById(R.id.nextButton);


        Type listType = new TypeToken<Page<Course>>() {}.getType();
        String results = requireArguments().getString("searchResult");
        mSearchQuery = requireArguments().getString("searchQuery");
        mCoursePage = (Page<Course>) (Object) mParserService.parseObject(results, listType);

        mContext = getActivity();
        mCourseService = new CourseService(mContext);

        if (mCoursePage.getTotalElements() == 0) {
            TextView noCourses = new TextView(mContext);
            noCourses.setText("No Courses Found!");
            mSearchResults.addView(noCourses);
        }


        mPreviousButton.setOnClickListener(v -> {
            if (mCoursePage.getNumber() > 0) {
                fetchCoursesForPage(null, mCoursePage.getNumber());
            }
            mNextButton.setVisibility(View.VISIBLE);
        });
        mNextButton.setOnClickListener(v -> {
            if (mCoursePage.getNumber() < mCoursePage.getTotalPages() - 1) {
                fetchCoursesForPage(null, mCoursePage.getNumber() + 2);
            }
            mPreviousButton.setVisibility(View.VISIBLE);
        });
        Log.d(TAG, results);
        addCourseTextAndShapes(mCoursePage);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!coldStart){
            if (mCurrentPage==0){
                fetchCoursesForPage(null, 1);
            }
           else{
                fetchCoursesForPage(null, mCurrentPage);
            }
        }
        coldStart = false;
    }

    public void updateFromFilter(JSONObject filtered){
        fetchCoursesForPage(filtered, 1);
        mCurrentPage = 1;
    }


    private void fetchCoursesForPage(JSONObject filterJson, int page){
        mSearchResults.removeAllViews();
        ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        mSearchResults.addView(progressBar);
        if (filterJson == null){
            Log.d(TAG, "filterJSON was null");
            LandingPageActivity activity = (LandingPageActivity) getActivity();
            filterJson = activity.getFilter();
        }
        mCurrentPage = page + 1;
        Log.d(TAG, "filterJSON was NOT NOT NOT null");
        mCourseService.semiGenericPOST(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Type listType = new TypeToken<Page<Course>>() {}.getType();
                        mCoursePage = (Page<Course>) (Object) mParserService.parseObject(result, listType);
                        addCourseTextAndShapes(mCoursePage);
                        Log.d("TAG","went to page "+page);
                    }
                },filterJson, "/filter/?name="+mSearchQuery+"&page="+page
        );
    }

    
    private void addCourseTextAndShapes(Page<Course> courseList){

        if (mCoursePage.isFirst()){
            mPreviousButton.setVisibility(View.INVISIBLE);
        }else{
            mPreviousButton.setVisibility(View.VISIBLE);
        }
        if (mCoursePage.isLast()){
            mNextButton.setVisibility(View.INVISIBLE);
        }else{
            mNextButton.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "I make it here");
        mSearchResults.removeAllViews();
        if (mCoursePage.getTotalElements() == 0) {
            TextView noCourses = new TextView(mContext);
            noCourses.setText("No Courses Found!");
            mSearchResults.addView(noCourses);
        }
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        shape.setColor(Color.rgb(60, 38, 204));
        for (Course course : courseList.getContent()) {
            Log.d(TAG, course.getName());
            //creating a container for one search result
            LinearLayout searchResultLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(16, 16, 16, 16);
            searchResultLayout.setLayoutParams(layoutParams);

            //create LinearLayout to display content vertically
            LinearLayout verticalLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams verticalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            verticalLayout.setLayoutParams(verticalParams);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);

            //creating a TextView for the course number with styling
            TextView courseNumberTextView = new TextView(mContext);
            courseNumberTextView.setText(course.getNumber());
            courseNumberTextView.setBackground(shape);
            courseNumberTextView.setPadding(12, 36, 12, 36);
            courseNumberTextView.setTextColor(Color.WHITE);
            searchResultLayout.addView(courseNumberTextView);

            //creating a TextView for the course name
            TextView nameTextView = new TextView(mContext);
            nameTextView.setText(course.getName());
            verticalLayout.addView(nameTextView);

            //creating a TextView for the course credits with guard for empty field
            Double credits = course.getCredits();
            if (credits != null) {
                TextView creditsTextView = new TextView(mContext);
                creditsTextView.setText((credits).toString() + " credits");
                verticalLayout.addView(creditsTextView);
            }

            //todo still needs guard for empty input like above
            String level = course.getLevel();
            TextView levelTextView = new TextView(mContext);
            levelTextView.setText(level);
            verticalLayout.addView(levelTextView);

            //create LinearLayout to display content horizontally
            LinearLayout horizontalLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams horizontalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            horizontalLayout.setLayoutParams(horizontalParams);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

            //create LinearLayout to display text next to ratings
            LinearLayout textLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textLayout.setLayoutParams(textParams);
            textLayout.setOrientation(LinearLayout.VERTICAL);


            //create LinearLayout for all ratings
            LinearLayout allRatingsLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams allRatingsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            allRatingsLayout.setLayoutParams(allRatingsLayoutParams);
            allRatingsLayout.setOrientation(LinearLayout.VERTICAL);

            Double[] ratings = {course.getTotalOverall(), course.getTotalDifficulty(), course.getTotalCourseMaterial(), course.getTotalWorkload(), course.getTotalTeachingQuality()};
            String[] headings = {"Overall Score", "Difficulty", "Material", "Workload", "Teaching Quality"};
            for (int j = 0; j < 5; j++) {
                TextView ratingTextView = new TextView(mContext);
                ratingTextView.setText(headings[j]);
                textLayout.addView(ratingTextView);

                double overAllRating = ratings[j] / course.getTotalReviews();
                LinearLayout ratingLayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ratingLayout.setLayoutParams(ratingParams);
                for (int i = 0; i < 5; i++) {
                    Drawable full = ResourcesCompat.getDrawable(getResources(), R.drawable.ratingdot_full, mContext.getTheme());
                    Drawable empty = ResourcesCompat.getDrawable(getResources(), R.drawable.ratingdot_empty, mContext.getTheme());
                    ImageView circleView = new ImageView(mContext);
                    if (overAllRating > 0.75) {
                        overAllRating--;
                        circleView.setBackground(full);
                    } else if (overAllRating > 0.25) {
                        overAllRating--;
                        ClipDrawable clipDrawable = new ClipDrawable(full, Gravity.LEFT, ClipDrawable.HORIZONTAL);
                        clipDrawable.setLevel(5000);
                        Drawable[] layers = {empty, clipDrawable};
                        LayerDrawable layerDrawable = new LayerDrawable(layers);
                        circleView.setBackground(layerDrawable);
                    } else {
                        circleView.setBackground(empty);
                    }
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                            50,
                            50//ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(4, 0, 4, 0);

                    circleView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    circleView.setPadding(3, 3, 3, 3);
                    circleView.setLayoutParams(params);

                    ratingLayout.addView(circleView);
                }
                allRatingsLayout.addView(ratingLayout);

            }

            horizontalLayout.addView(textLayout);
            horizontalLayout.addView(allRatingsLayout);
            verticalLayout.addView(horizontalLayout);
            searchResultLayout.addView(verticalLayout);
            //creating a onclick listener for the search result
            searchResultLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    List<Object> courseList = new ArrayList<>();
                    //                    courseList.add(course);
                    Intent intent = new Intent(mContext, CourseActivity.class);
                    intent.putExtra("course", mParserService.deParseObject(course));
                    mContext.startActivity(intent);
                }
            });
            //adding the search result to the fragment
            mSearchResults.addView(searchResultLayout);
        }
    }

    public void updateSearchQuery(String toString) {
        mSearchQuery = toString;
        mCurrentPage = 1;
    }
}
