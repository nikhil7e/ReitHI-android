package is.hi.hbv601g.reithi_android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.reithi_android.Activities.CourseActivity;
import is.hi.hbv601g.reithi_android.Activities.ReviewPageActivity;
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
        List<Course> courseList = (List<Course>) (Object) mParserService.parse(results, listType);
        Context context = getActivity();
        if (courseList.size() == 0) {
            TextView noCourses = new TextView(context);
            noCourses.setText("No Courses Found!");
            mSearchResults.addView(noCourses);
        }
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        shape.setColor(Color.rgb(60, 38, 204));

        for (Course course : courseList) {

            //creating a container for one search result
            LinearLayout searchResultLayout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(16, 16, 16, 16);
            searchResultLayout.setLayoutParams(layoutParams);

            //create LinearLayout to display content vertically
            LinearLayout verticalLayout = new LinearLayout(context);
            LinearLayout.LayoutParams verticalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            verticalLayout.setLayoutParams(verticalParams);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);

            //creating a TextView for the course number with styling
            TextView courseNumberTextView = new TextView(context);
            courseNumberTextView.setText(course.getNumber());
            courseNumberTextView.setBackground(shape);
            courseNumberTextView.setPadding(12, 36, 12, 36);
            courseNumberTextView.setTextColor(Color.WHITE);
            searchResultLayout.addView(courseNumberTextView);

            //creating a TextView for the course name
            TextView nameTextView = new TextView(context);
            nameTextView.setText(course.getName());
            verticalLayout.addView(nameTextView);

            //creating a TextView for the course credits with guard for empty field
            Double credits = course.getCredits();
            if (credits != null) {
                TextView creditsTextView = new TextView(context);
                creditsTextView.setText((credits).toString() + " credits");
                verticalLayout.addView(creditsTextView);
            }
            //still needs guard for empty input
            String level = course.getLevel();
            TextView levelTextView = new TextView(context);
            levelTextView.setText(level);
            verticalLayout.addView(levelTextView);

            //create LinearLayout to display content horizontally
            LinearLayout horizontalLayout = new LinearLayout(context);
            LinearLayout.LayoutParams horizontalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            horizontalLayout.setLayoutParams(horizontalParams);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

            //create LinearLayout to display text next to ratings
            LinearLayout textLayout = new LinearLayout(context);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textLayout.setLayoutParams(textParams);
            textLayout.setOrientation(LinearLayout.VERTICAL);


            //create LinearLayout for all ratings
            LinearLayout allRatingsLayout = new LinearLayout(context);
            LinearLayout.LayoutParams allRatingsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            allRatingsLayout.setLayoutParams(allRatingsLayoutParams);
            allRatingsLayout.setOrientation(LinearLayout.VERTICAL);

            Double[] ratings = {course.getTotalOverall(), course.getTotalDifficulty(), course.getTotalCourseMaterial(), course.getTotalWorkload(), course.getTotalTeachingQuality()};
            String[] headings = {"Overall Score", "Difficulty","Material","Workload","Teaching Quality"};
            for (int j = 0; j < 5; j++) {
                TextView ratingTextView = new TextView(context);
                ratingTextView.setText(headings[j]);
                textLayout.addView(ratingTextView);

                double overAllRating = ratings[j] / course.getTotalReviews();
                LinearLayout ratingLayout = new LinearLayout(context);
                LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ratingLayout.setLayoutParams(ratingParams);
                for (int i = 0; i < 5; i++) {
                    ShapeDrawable circle = new ShapeDrawable(new OvalShape());
                    Drawable full = getResources().getDrawable(R.drawable.ratingdot_full);
                    Drawable empty = getResources().getDrawable(R.drawable.ratingdot_empty);
                    ImageView circleView = new ImageView(context);
                    if (overAllRating > 0.75) {
                        overAllRating--;
                        circleView.setBackground(full);
                    } else if (overAllRating > 0.25) {
                        overAllRating--;
                        ClipDrawable clipDrawable = new ClipDrawable(full, Gravity.LEFT, ClipDrawable.HORIZONTAL);
                        clipDrawable.setLevel(5000);
                        ClipDrawable clipDrawable2 = new ClipDrawable(empty, Gravity.RIGHT, ClipDrawable.HORIZONTAL);
                        clipDrawable.setLevel(5000);
                        Drawable[] layers = {clipDrawable, clipDrawable2};
                        LayerDrawable layerDrawable = new LayerDrawable(layers);
                        circleView.setBackground(layerDrawable);
                    } else {
                        circleView.setBackground(empty);
                    }
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                            50,
                            50//ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                  /*  params.setMargins(1,1,1,1);*/

                    circleView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    circleView.setPadding(3,3,3,3);
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
                    List<Object> courseList = new ArrayList<>();
                    courseList.add(course);
                    Intent intent = new Intent(context, CourseActivity.class);
                    intent.putExtra("course", mParserService.deParse(courseList));
                    context.startActivity(intent);
                }
            });
            //adding the search result to the fragment
            mSearchResults.addView(searchResultLayout);
        }

    }

}
