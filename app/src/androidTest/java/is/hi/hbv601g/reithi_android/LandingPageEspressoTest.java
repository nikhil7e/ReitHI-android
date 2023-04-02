package is.hi.hbv601g.reithi_android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LandingPageEspressoTest {

    @Rule
    public ActivityScenarioRule<LandingPageActivity> activityRule = new ActivityScenarioRule<>(LandingPageActivity.class);

    @Test
    public void searchBar_visible() {
        onView(withId(R.id.search_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void searchButton_visible() {
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
    }

//    @Test
//    public void searchButton_click() {
//        onView(withId(R.id.search_button)).perform(click());
//        // Add assertion for the expected behavior after search button is clicked
//    }

    @Test
    public void searchResults_containsTitleWithSearchQuery() throws InterruptedException {
        String searchQuery = "9/11";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));
        onView(withId(R.id.search_button)).perform(click());

        Thread.sleep(5000); // wait for 5 seconds

        onView(withId(R.id.search_results)).check(matches(isDisplayed()));

//        onView(allOf(withId(R.id.nameTextView), isDescendantOfA(withId(R.id.search_results))))
//                .check(matches(allOf(isDisplayed())))
//                .check(matches(allOf(withText(containsString(searchQuery)))));

        onView(withId(R.id.search_results))
                .check(matches(isDisplayed()))
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
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
                    }
                });



//        onView(allOf(withId(R.id.nameTextView), isDisplayed(), isDescendantOfA(withId(R.id.search_results))))
//                .check(matches(allOf(isDisplayed(), withText(containsString(searchQuery)))));

    }

}