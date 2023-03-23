package is.hi.hbv601g.reithi_android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.RangeSlider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Activities.LoginActivity;
import is.hi.hbv601g.reithi_android.Activities.ReviewPageActivity;
import is.hi.hbv601g.reithi_android.Entities.FilterSearch;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.CourseService;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class FilterFragment extends Fragment {

    private ParserService mParserService;

    private CourseService mCourseService;

    private final String TAG = "FilterFragment";

    private Button applyFilter;

    public FilterFragment() {
        super(R.layout.fragment_filter);
    }

    private Switch graduateSwitch;
    private Switch undergraduateSwitch;
    private RangeSlider creditRangeSlider;
    private RangeSlider overallRangeSlider;
    private RangeSlider difficultyRangeSlider;
    private RangeSlider workloadRangeSlider;
    private RangeSlider TQSlider;
    private RangeSlider CMSlider;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        mParserService = ParserService.getInstance();
        mCourseService = new CourseService(getActivity());
        applyFilter = view.findViewById(R.id.applyFilterButton);
        graduateSwitch = view.findViewById(R.id.graduateSwitch);
        undergraduateSwitch = view.findViewById(R.id.undergraduate_switch);
        creditRangeSlider = view.findViewById(R.id.credits_range_slider);
        overallRangeSlider = view.findViewById(R.id.overall_score_range_slider);
        difficultyRangeSlider = view.findViewById(R.id.difficulty_range_slider);
        workloadRangeSlider = view.findViewById(R.id.workload_range_slider);
        TQSlider = view.findViewById(R.id.teaching_quality_range_slider);
        CMSlider = view.findViewById(R.id.course_material_range_slider);
        applyFilter.setOnClickListener(v -> {
            JSONObject jsonBody = getFilterObject();
            LandingPageActivity activity = (LandingPageActivity) getActivity();
            activity.forwardFilter(jsonBody);
            /*mCourseService.filterPOST(new NetworkCallback<String>() {

                @Override
                public void onFailure(String errorString) {
                    Log.e(TAG, errorString);
                }

                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Filter successfully created");
                    Bundle bundle = new Bundle();
                    bundle.putString("filtered", result);

                    LandingPageActivity activity = (LandingPageActivity) getActivity();
                    activity.forwardFilter(result);
                }
            }, jsonBody, "/filter");*/
        });
    }

    public JSONObject getFilterObject(){

        RangeSlider[] sliders = {creditRangeSlider, overallRangeSlider, difficultyRangeSlider, workloadRangeSlider, TQSlider, CMSlider};

        FilterSearch filter = new FilterSearch();

        if (graduateSwitch.isChecked() && !undergraduateSwitch.isChecked()) {
            filter.setGraduate(true);
        } else if (!graduateSwitch.isChecked() && undergraduateSwitch.isChecked()) {
            filter.setUndergraduate(true);
        }
        filter.setCreditsRange(new Integer[]{0, 300});

        for (int i = 0; i < sliders.length; i++) {
            RangeSlider slider = sliders[i];
            List<Float> currentValues = slider.getValues();

            float min = currentValues.get(0);
            float max = currentValues.get(1);
            int minValue = (int) min;
            int maxValue = (int) max;
            Integer[] params = new Integer[]{minValue, maxValue};
            if (!(minValue == 0.0 && maxValue == 5.0)){
                switch (i) {
                    case 0:
                        if(!(minValue == 0.0 && maxValue == 30.0)){
                            filter.setCreditsRange(params);
                        }break;
                    case 1:
                        filter.setOverallRange(params);break;
                    case 2:
                        filter.setDifficultyRange(params);break;
                    case 3:
                        filter.setWorloadRange(params);break;
                    case 4:
                        filter.setTeachingQualityRange(params);break;
                    case 5:
                        filter.setCourseMaterialRange(params);break;
                }
            }
        }
        Log.d(TAG, filter.toString());
        String filterString = mParserService.deParseObject(filter);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("filter", filterString);
            return jsonBody;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
