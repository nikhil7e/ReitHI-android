package is.hi.hbv601g.reithi_android.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import is.hi.hbv601g.reithi_android.Entities.FilterSearch;
import is.hi.hbv601g.reithi_android.NetworkManager;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ParserService mParserService;

    private NetworkManager mNetworkManager;
    private Button applyFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mParserService = ParserService.getInstance();

        applyFilter = findViewById(R.id.applyFilterButton);
        applyFilter.setOnClickListener(v -> {
            Switch graduateSwitch = findViewById(R.id.graduateSwitch);
            Switch undergraduateSwitch = findViewById(R.id.undergraduate_switch);
            RangeSlider creditRangeSlider = findViewById(R.id.credits_range_slider);
            RangeSlider overallRangeSlider = findViewById(R.id.overall_score_range_slider);
            RangeSlider difficultyRangeSlider = findViewById(R.id.difficulty_range_slider);
            RangeSlider workloadRangeSlider = findViewById(R.id.workload_range_slider);
            RangeSlider TQSlider = findViewById(R.id.teaching_quality_range_slider);
            RangeSlider CMSlider = findViewById(R.id.course_material_range_slider);
            RangeSlider[] sliders = {creditRangeSlider, overallRangeSlider, difficultyRangeSlider, workloadRangeSlider, TQSlider, CMSlider};

            FilterSearch filter = new FilterSearch();
            if (graduateSwitch.isChecked() && !undergraduateSwitch.isChecked()) {
                filter.setGraduate(true);
            } else if (!graduateSwitch.isChecked() && undergraduateSwitch.isChecked()) {
                filter.setUndergraduate(true);
            }

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
        });
    }
}
