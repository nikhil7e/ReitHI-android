package is.hi.hbv601g.reithi_android;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;

@LargeTest
public class LandingPageEspressoTest {

    @Rule
    public ActivityScenarioRule<LandingPageActivity> activityRule = new ActivityScenarioRule<>(LandingPageActivity.class);

    @Test
    public void testButtonClick() {
        Espresso.onView(ViewMatchers.withId(R.id.search_button)).perform(ViewActions.click());
    }
}