package is.hi.hbv601g.reithi_android;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import static is.hi.hbv601g.reithi_android.EspressoUtils.waitId;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LandingPageEspressoTest {

    @Rule
    public ActivityScenarioRule<LandingPageActivity> activityRule = new ActivityScenarioRule<>(LandingPageActivity.class);

//    @Rule
//    public ActivityTestRule<LandingPageActivity> activityTestRule = new ActivityTestRule<>(LandingPageActivity.class);

    @Test
    public void searchResultsContainTitleWithEmptySearchQuery() {
        String searchQuery = "";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

        onView(isRoot()).perform(waitId(R.id.search_results, 10000));

        onView(withId(R.id.search_results))
                .check((view, noViewFoundException) -> {
                    if (view instanceof LinearLayout) {
                        LinearLayout linearLayout = (LinearLayout) view;
                        int childCount = linearLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View childView = linearLayout.getChildAt(i);
                            TextView textView = childView.findViewById(R.id.nameTextView);
                            String text = textView.getText().toString().toLowerCase();
                            assertThat(text, containsString(searchQuery.toLowerCase()));
                        }
                    } else {
                        throw new AssertionError("View is not a LinearLayout");
                    }
                });
    }

    @Test
    public void searchResultsContainTitleWithSearchQuery() {
        String searchQuery = "Test";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

        onView(isRoot()).perform(waitId(R.id.search_results, 10000));

        onView(withId(R.id.search_results))
                .check((view, noViewFoundException) -> {
                    if (view instanceof LinearLayout) {
                        LinearLayout linearLayout = (LinearLayout) view;
                        int childCount = linearLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View childView = linearLayout.getChildAt(i);
                            TextView textView = childView.findViewById(R.id.nameTextView);
                            String text = textView.getText().toString().toLowerCase();
                            assertThat(text, containsString(searchQuery.toLowerCase()));
                        }
                    } else {
                        throw new AssertionError("View is not a LinearLayout");
                    }
                });
    }

    @Test
    public void searchResultsContainNoCourses() {
        String searchQuery = "//";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

        onView(isRoot()).perform(waitId(R.id.search_results, 10000));


        onView(withId(R.id.search_results))
                .check(matches(hasDescendant(allOf(
                        instanceOf(TextView.class),
                        withText("No Courses Found!")
                ))));
    }


}