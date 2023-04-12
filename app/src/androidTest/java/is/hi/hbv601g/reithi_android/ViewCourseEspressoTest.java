package is.hi.hbv601g.reithi_android;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;
import static is.hi.hbv601g.reithi_android.EspressoUtils.waitForViewToBeDisplayed;
import static is.hi.hbv601g.reithi_android.EspressoUtils.waitForViewToBeUpdated;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.internal.util.Checks;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
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

        waitForViewToBeDisplayed(R.id.search_results);

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

        waitForViewToBeDisplayed(R.id.username_text_view);

        onView(withId(R.id.homeButton)).perform(click());

        String searchQuery = "Modern";
        onView(withId(R.id.search_bar)).perform(typeText(searchQuery));

        onView(withId(R.id.search_button)).perform(click());

        waitForViewToBeDisplayed(R.id.search_results);

        ViewInteraction firstChild = onView(
                allOf(
                        withParent(allOf(
                                withId(R.id.search_results),
                                isDisplayed()
                        )),
                        isDisplayed(),
                        withParentIndex(0)
                )
        );

        firstChild.perform(click());
        onView(withId(R.id.review_button)).perform(click());

        double SLIDER_VALUE = 5.0;

        onView(allOf(withId(R.id.slider), isDescendantOfA(withId(R.id.slider_1)))).perform(actionWithAssertions(setSliderValue(SLIDER_VALUE)));
        onView(allOf(withId(R.id.slider), isDescendantOfA(withId(R.id.slider_2)))).perform(actionWithAssertions(setSliderValue(SLIDER_VALUE)));
        onView(allOf(withId(R.id.slider), isDescendantOfA(withId(R.id.slider_3)))).perform(actionWithAssertions(setSliderValue(SLIDER_VALUE)));
        onView(allOf(withId(R.id.slider), isDescendantOfA(withId(R.id.slider_4)))).perform(actionWithAssertions(setSliderValue(SLIDER_VALUE)));
        onView(allOf(withId(R.id.slider), isDescendantOfA(withId(R.id.slider_5)))).perform(actionWithAssertions(setSliderValue(SLIDER_VALUE)));

        onView(withId(R.id.comment)).perform(typeText("Test comment"), closeSoftKeyboard());
        onView(withId(R.id.submit_review_button)).check(matches(isDisplayed())).perform(click());

        waitForViewToBeDisplayed(R.id.overallTextViewLabel);

        onView(withId(R.id.overallscoretext)).check(matches(withText(String.valueOf(SLIDER_VALUE))));
        onView(withId(R.id.difficultytext)).check(matches(withText(String.valueOf(SLIDER_VALUE))));
        onView(withId(R.id.teachingqualitytext)).check(matches(withText(String.valueOf(SLIDER_VALUE))));
        onView(withId(R.id.workloadtext)).check(matches(withText(String.valueOf(SLIDER_VALUE))));
        onView(withId(R.id.materialtext)).check(matches(withText(String.valueOf(SLIDER_VALUE))));

        onView(withId(R.id.read_reviews_button)).perform(click());

        // TODO: tekka hvort course name matchi við course name á read reviews

        waitForViewToBeDisplayed(R.id.all_Reviews);

        Matcher<View> parentMatcher = allOf(
                withParent(allOf(
                        withId(R.id.all_Reviews),
                        isDisplayed()
                )),
                withId(R.id.review_layout),
                hasDescendant(allOf(
                        isDisplayed(),
                        withId(R.id.review_title),
                        withText(username)
                )),
                isDisplayed(),
                withParentIndex(0)
        );

        onView(
                allOf(
                        isDescendantOfA(parentMatcher),
                        withId(R.id.upvote_button)
                )
        ).perform(click());

        waitForViewToBeUpdated(R.id.upvotes_downvotes_text, "1");

        onView(
                allOf(
                        isDisplayed(),
                        isDescendantOfA(parentMatcher),
                        withId(R.id.upvotes_downvotes_text),
                        withText("1")
                )
        ).perform(click());

        onView(
                allOf(
                        isDescendantOfA(parentMatcher),
                        withId(R.id.downvote_button)
                )
        ).perform(click());

        waitForViewToBeUpdated(R.id.upvotes_downvotes_text, "-1");

        onView(
                allOf(
                        isDisplayed(),
                        isDescendantOfA(parentMatcher),
                        withId(R.id.upvotes_downvotes_text),
                        withText("-1")
                )
        ).perform(click());

        onView(withId(R.id.userButton)).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.reviews_button)).perform(click());

        waitForViewToBeDisplayed(R.id.all_Reviews);

        onView(withId(R.id.delete_review_button)).perform(click());
        onView(withText("Delete")).perform(click());
        onView(withText("Review successfully deleted")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    private static ViewAction setSliderValue(final double value) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription() {
                return "Set the value on a SeekBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress((int) value);
            }
        };
    }

    public static Matcher<View> withBgColor(final int color) {
        return new BoundedMatcher<View, LinearLayout>(LinearLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: ");
                description.appendValue(color);
            }

            @Override
            protected boolean matchesSafely(LinearLayout linearLayout) {
                return linearLayout.getBackground() instanceof ColorDrawable &&
                        ((ColorDrawable) linearLayout.getBackground()).getColor() == color;
            }
        };
    }


}
