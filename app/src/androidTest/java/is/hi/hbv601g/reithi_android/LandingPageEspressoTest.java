package is.hi.hbv601g.reithi_android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LandingPageEspressoTest {

    @Rule
    public ActivityScenarioRule<LandingPageActivity> activityRule = new ActivityScenarioRule<>(LandingPageActivity.class);

    @Rule
    public ActivityTestRule<LandingPageActivity> activityTestRule = new ActivityTestRule<>(LandingPageActivity.class);

//    @Rule
//    public IdlingRegistry idlingRegistry = IdlingRegistry.getInstance();

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

//    @Test
//    public void searchResultsContainsTitleWithSearchQuery() throws InterruptedException {
//        String searchQuery = "Software";
//        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));
//        onView(withId(R.id.search_button)).perform(click());
//
//        Thread.sleep(5000); // wait for 5 seconds
//
//        onView(withId(R.id.search_results))
//                .check(matches(isDisplayed()))
//                .check(new ViewAssertion() {
//                    @Override
//                    public void check(View view, NoMatchingViewException noViewFoundException) {
//                        if (view instanceof LinearLayout) {
//                            LinearLayout linearLayout = (LinearLayout) view;
//                            int childCount = linearLayout.getChildCount();
//                            for (int i = 0; i < childCount; i++) {
//                                View childView = linearLayout.getChildAt(i);
//                                TextView textView = childView.findViewById(R.id.nameTextView);
//                                String text = textView.getText().toString().toLowerCase();
//                                assertThat(text, containsString(searchQuery.toLowerCase()));
//                            }
//                        } else {
//                            throw new AssertionError("View is not a LinearLayout");
//                        }
//                    }
//                });
//    }
//
//    @Test
//    public void searchResultsContainsTitleWithInvalidSearchQuery() throws InterruptedException {
//        String searchQuery = "-+|:{";
//        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));
//        onView(withId(R.id.search_button)).perform(click());
//
//        Thread.sleep(5000); // wait for 5 seconds
//
//        onView(withId(R.id.search_results))
//                .check(matches(isDisplayed()))
//                .check(new ViewAssertion() {
//                    @Override
//                    public void check(View view, NoMatchingViewException noViewFoundException) {
//                        if (view instanceof LinearLayout) {
//                            LinearLayout linearLayout = (LinearLayout) view;
//                            int childCount = linearLayout.getChildCount();
//                            for (int i = 0; i < childCount; i++) {
//                                View childView = linearLayout.getChildAt(i);
//                                TextView textView = childView.findViewById(R.id.nameTextView);
//                                String text = textView.getText().toString().toLowerCase();
//                                assertThat(text, containsString(searchQuery.toLowerCase()));
//                            }
//                        } else {
//                            throw new AssertionError("View is not a LinearLayout");
//                        }
//                    }
//                });
//    }

    @Test
    public void searchResultsContainsTitleWithEmptySearchQuery() throws InterruptedException {
        String searchQuery = "";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

//        LandingPageActivity activity = activityTestRule.getActivity();
//        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.searchResultLayout);
//
//        FragmentLoadingIdlingResource idlingResource = new FragmentLoadingIdlingResource(
//                activityTestRule.getActivity().getSupportFragmentManager(),
//                fragment
//        );

//        Espresso.registerIdlingResources(idlingResource);

        Thread.sleep(5000);

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

//        Espresso.unregisterIdlingResources(idlingResource);
    }

}