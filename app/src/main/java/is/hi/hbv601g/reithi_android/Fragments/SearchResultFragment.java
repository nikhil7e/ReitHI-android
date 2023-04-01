package is.hi.hbv601g.reithi_android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;

import is.hi.hbv601g.reithi_android.Activities.CourseActivity;
import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Activities.ReadReviewsActivity;
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
        LandingPageActivity activity = (LandingPageActivity) getActivity();
        activity.hideShimmer();
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
        /*ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        mSearchResults.addView(progressBar);*/
        LandingPageActivity activity = (LandingPageActivity) getActivity();
        activity.showShimmer();
        if (filterJson == null){
            Log.d(TAG, "filterJSON was null");
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
                        LandingPageActivity activity = (LandingPageActivity) getActivity();
                        activity.hideShimmer();
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
        Resources res = getResources();
        String[] schools = res.getStringArray(R.array.faculty_list);

        for (Course course : courseList.getContent()) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View searchResultLayout= inflater.inflate(R.layout.course_element_layout, null);
            TextView courseNum = searchResultLayout.findViewById(R.id.courseNumberTextView);
            courseNum.setText(course.getNumber());
            TextView courseSchool = searchResultLayout.findViewById(R.id.schoolTextView);
            if (Arrays.asList(schools).contains(course.getSchool())) {
                courseSchool.setText(course.getSchool());
            }
            TextView courseName = searchResultLayout.findViewById(R.id.nameTextView);
            courseName.setText(course.getName());
            TextView courseLevel = searchResultLayout.findViewById(R.id.levelTextView);
            courseLevel.setText(course.getLevel());
            TextView courseCredits = searchResultLayout.findViewById(R.id.creditsTextView);
            courseCredits.setText(course.getCredits()+" credits");

            LinearLayout overAllLayout = searchResultLayout.findViewById(R.id.overallRatingsLayout);
            LinearLayout workloadLayout = searchResultLayout.findViewById(R.id.workloadRatingsLayout);
            LinearLayout difficultyLayout = searchResultLayout.findViewById(R.id.difficultyRatingsLayout);
            LinearLayout materialLayout = searchResultLayout.findViewById(R.id.materialRatingsLayout);
            LinearLayout tqLayout = searchResultLayout.findViewById(R.id.tqRatingsLayout);
            colorDots(overAllLayout, course.getTotalOverall()/course.getTotalReviews());
            colorDots(workloadLayout, course.getTotalWorkload()/course.getTotalReviews());
            colorDots(difficultyLayout, course.getTotalDifficulty()/course.getTotalReviews());
            colorDots(materialLayout, course.getTotalCourseMaterial()/course.getTotalReviews());
            colorDots(tqLayout, course.getTotalTeachingQuality()/course.getTotalReviews());
            //creating a onclick listener for the search result
            searchResultLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public void colorDots(LinearLayout container, Double score){
        int childCount = container.getChildCount();
        Drawable ratingDotFull = ResourcesCompat.getDrawable(getResources(), R.drawable.ratingdot_full, mContext.getTheme());
        Drawable ratingDotEmpty = ResourcesCompat.getDrawable(getResources(), R.drawable.ratingdot_empty, mContext.getTheme());
        for (int i = 0; i < childCount; i++) {
            View childView = container.getChildAt(i);
            if (childView instanceof ImageView) {
                ImageView imageView = (ImageView) childView;
                if(score > 0.75){
                    imageView.setImageDrawable(ratingDotFull);
                } else if (score >0.25) {
                    ClipDrawable clipDrawable = new ClipDrawable(ratingDotFull, Gravity.LEFT, ClipDrawable.HORIZONTAL);
                    clipDrawable.setLevel(5000);
                    Drawable[] layers = {ratingDotEmpty, clipDrawable};
                    LayerDrawable layerDrawable = new LayerDrawable(layers);
                    imageView.setImageDrawable(layerDrawable);
                }
                score--;
            }
        }
    }
}
