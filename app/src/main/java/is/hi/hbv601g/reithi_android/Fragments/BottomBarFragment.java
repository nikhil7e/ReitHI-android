package is.hi.hbv601g.reithi_android.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.reithi_android.Activities.AccountActivity;
import is.hi.hbv601g.reithi_android.Activities.CourseActivity;
import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Activities.LoginActivity;
import is.hi.hbv601g.reithi_android.Activities.ReadReviewsActivity;
import is.hi.hbv601g.reithi_android.Activities.SignupActivity;
import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.R;
import is.hi.hbv601g.reithi_android.Services.ParserService;

public class BottomBarFragment extends Fragment {

    private ParserService mParserService;

    private final String TAG = "SearchResultFragment";
    private ImageButton mLoginAndAccountButton;
    private ImageButton mHomeButton;

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        /** Inflating the layout for this fragment **/
//        View v = inflater.inflate(R.layout.fragment_search_result, null);
//        return v;
//    }
    public BottomBarFragment() {
        super(R.layout.fragment_navigation_bar);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mLoginAndAccountButton = view.findViewById(R.id.userButton);
        mLoginAndAccountButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySession", MODE_PRIVATE);
            String userString = sharedPreferences.getString("loggedInUser", "");

            if (userString != ""){
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }

        });

        mHomeButton = view.findViewById(R.id.homeButton);
        mHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LandingPageActivity.class);
            startActivity(intent);
        });
        setUserIcon(false);
        setHomeIcon(false);
        if (getActivity() instanceof AccountActivity || getActivity() instanceof LoginActivity || getActivity() instanceof SignupActivity) {
            setUserIcon(true);
        } else if(getActivity() instanceof LandingPageActivity){
            setHomeIcon(true);
        }
    }
    public void setUserIcon(Boolean isInAccount){
        if (isInAccount){
            Drawable userFilled = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.user_icon_svg_test_filled, getActivity().getTheme());
            mLoginAndAccountButton.setImageDrawable(userFilled);
            mLoginAndAccountButton.setEnabled(false);
        }
        else{
            Drawable user= ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.user_icon_svg_test, getActivity().getTheme());
            mLoginAndAccountButton.setImageDrawable(user);
            mLoginAndAccountButton.setEnabled(true);
        }
    }

    public void setHomeIcon(Boolean isInHome){
        if (isInHome){
            Drawable userFilled = ResourcesCompat.getDrawable(getResources(), R.drawable.home_icon_filled, getActivity().getTheme());
            mHomeButton.setImageDrawable(userFilled);
            mHomeButton.setEnabled(false);
        }
        else{
            Drawable user= ResourcesCompat.getDrawable(getResources(), R.drawable.home_icon, getActivity().getTheme());
            mHomeButton.setImageDrawable(user);
            mHomeButton.setEnabled(true);
        }
    }

}
