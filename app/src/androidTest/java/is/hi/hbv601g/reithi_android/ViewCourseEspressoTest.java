package is.hi.hbv601g.reithi_android;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;



import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static is.hi.hbv601g.reithi_android.EspressoUtils.waitId;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Services.UserService;

public class ViewCourseEspressoTest {

    private UserService mUserService;
    private String username = "TestUser5";

    @Rule
    public ActivityTestRule<LandingPageActivity> activityTestRule = new ActivityTestRule<>(LandingPageActivity.class);

    @Rule
    public DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Before
    public void setUp() {
        mUserService = new UserService(activityTestRule.getActivity());
        mUserService.genericUserGET(new NetworkCallback<>() {
            @Override
            public void onFailure(String errorString) {
                Log.d("TESTING", errorString);
            }

            @Override
            public void onSuccess(String json) {
                Log.d("TESTING", json);
            }
        }, "/deleteuser/?username=" + username);

        SharedPreferences sharedPreferences = activityTestRule.getActivity().getSharedPreferences("MySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @After
    public void tearDown() {
        mUserService = new UserService(activityTestRule.getActivity());
        mUserService.genericUserGET(new NetworkCallback<>() {
            @Override
            public void onFailure(String errorString) {
                Log.d("TESTING", errorString);
            }

            @Override
            public void onSuccess(String json) {
                Log.d("TESTING", json);
            }
        }, "/deleteuser/?username=" + username);

        SharedPreferences sharedPreferences = activityTestRule.getActivity().getSharedPreferences("MySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    // TODO: check units, scores etc, not just course title?
    @Test
    public void coursePageMatchesSelectCourseTest() throws InterruptedException {
        String searchQuery = "9/11";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

        final CountDownLatch latch = new CountDownLatch(1);
        long endTime = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(R.id.search_results)).check(matches(isDisplayed()));
                latch.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for search results to be displayed");
        }

        onData(withId(R.id.search_results));
        // onView(isRoot()).perform(waitId(R.id.search_results, 10000));

        // Thread.sleep(5000);

        String[] expectedValues = new String[2]; // Declare array to store expected values

        ViewInteraction firstChild = onView(
                allOf(
                        withParent(allOf(
                                withId(R.id.search_results),
                                isDisplayed()
                        )),
                        isDisplayed()
                )
        );

        firstChild.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(LinearLayout.class);
            }

            @Override
            public String getDescription() {
                return "getting text from TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView courseNameTextView = view.findViewById(R.id.nameTextView);
                expectedValues[0] = courseNameTextView.getText().toString();
                Log.d("TESTING", expectedValues[0]);
            }
        });

        firstChild.perform(click());
        //IdlingRegistry.getInstance().unregister(idlingResourceLogout);

        onView(
                allOf(
                        withId(R.id.courseName), withText(expectedValues[0]))).check(matches(isDisplayed()));
    }

    @Test
    public void reviewCourseAndDeleteTest() throws InterruptedException {
        onView(withId(R.id.userButton)).perform(click());
        onView(withId(R.id.signup_button)).perform(click());

        onView(withId(R.id.userName_input)).perform(typeText(username));
        onView(withId(R.id.password_input)).perform(typeText("123"));
        onView(withId(R.id.password_verify_input)).perform(typeText("123"));
        onView(withId(R.id.signup_button)).perform(click());

        final CountDownLatch latch = new CountDownLatch(1);
        long endTime = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(R.id.username_text_view)).check(matches(isDisplayed()));
                latch.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for search results to be displayed");
        }

        onView(withId(R.id.homeButton)).perform(click());

        String searchQuery = "Research with children and youths";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

        final CountDownLatch latch2 = new CountDownLatch(1);
        long endTime2 = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime2) {
            try {
                onView(withId(R.id.search_results)).check(matches(isDisplayed()));
                latch2.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch2.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for search results to be displayed");
        }

        // TODO: er ekki að velja fyrsta
        ViewInteraction firstChild = onView(
                allOf(
                        withParent(allOf(
                                withId(R.id.search_results),
                                isDisplayed()
                        )),
                        isDisplayed()
                )
        );

        firstChild.perform(click());
        onView(withId(R.id.review_button)).perform(click());

        // TODO: afh ekki haegt ad setja comment??
        // onView(withId(R.id.comment)).perform(typeText("Test comment"));
        onView(withId(R.id.submit_review_button)).perform(click());

        final CountDownLatch latch3 = new CountDownLatch(1);
        long endTime3 = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime3) {
            try {
                onView(withId(R.id.overallTextViewLabel)).check(matches(isDisplayed()));
                latch3.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch3.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for search results to be displayed");
        }

        onView(withId(R.id.overallscoretext)).check(matches(withText("1.0")));
        onView(withId(R.id.difficultytext)).check(matches(withText("1.0")));
        onView(withId(R.id.teachingqualitytext)).check(matches(withText("1.0")));
        onView(withId(R.id.workloadtext)).check(matches(withText("1.0")));
        onView(withId(R.id.materialtext)).check(matches(withText("1.0")));

        onView(withId(R.id.read_reviews_button)).perform(click());

        final CountDownLatch latch4 = new CountDownLatch(1);
        long endTime4 = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime4) {
            try {
                onView(withId(R.id.reviewCourseName)).check(matches(isDisplayed()));
                latch4.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch4.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for search results to be displayed");
        }

        onView(withId(R.id.delete_review_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withText("Review successfully deleted")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }


}
