package is.hi.hbv601g.reithi_android;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.SharedPreferences;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import is.hi.hbv601g.reithi_android.Activities.AccountActivity;
import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Activities.LoginActivity;
import is.hi.hbv601g.reithi_android.Services.UserService;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountEspressoTest {

    private UserService mUserService;
    private String username = "TestUser3";

//    @Rule
//    public ActivityScenarioRule<LandingPageActivity> activityRule = new ActivityScenarioRule<>(LandingPageActivity.class);

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
                System.out.print(errorString);
            }

            @Override
            public void onSuccess(String json) {
                System.out.print("Successful user delete");
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
        mUserService.genericUserGET(new NetworkCallback<String>() {
            @Override
            public void onFailure(String errorString) {
                System.out.print(errorString);
            }

            @Override
            public void onSuccess(String json) {
                System.out.print("Successful user delete");
            }
        }, "/deleteuser/?username=" + username);

        SharedPreferences sharedPreferences = activityTestRule.getActivity().getSharedPreferences("MySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Test
    public void signupTest() {
        onView(withId(R.id.userButton)).perform(click());
        onView(withId(R.id.signup_button)).perform(click());

        onView(withId(R.id.userName_input)).perform(typeText(username));
        onView(withId(R.id.password_input)).perform(typeText("123"));
        onView(withId(R.id.password_verify_input)).perform(typeText("123"));
        onView(withId(R.id.signup_button)).perform(click());

        IdlingRegistry.getInstance().register(new ActivityIdlingResource(AccountActivity.class));
        onView(withId(R.id.username_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.username_text_view)).check(matches(withText(username)));
        IdlingRegistry.getInstance().unregister(new ActivityIdlingResource(AccountActivity.class));
    }

    @Test
    public void logoutTest() {
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
                onView(withId(R.id.logout_button)).check(matches(isDisplayed()));
                latch.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        onView(withId(R.id.logout_button)).perform(click());

        final CountDownLatch latch2 = new CountDownLatch(1);
        long endTime2 = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime2) {
            try {
                onView(withId(R.id.login_button)).check(matches(isDisplayed()));
                latch2.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }

        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void loginTest() {
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
                onView(withId(R.id.logout_button)).check(matches(isDisplayed()));
                latch.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }

        onView(withId(R.id.logout_button)).perform(click());

        onView(withId(R.id.userName_input)).perform(typeText(username));
        onView(withId(R.id.password_input)).perform(typeText("123"));
        onView(withId(R.id.login_button)).perform(click());

        final CountDownLatch latch2 = new CountDownLatch(1);
        long endTime2 = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime2) {
            try {
                onView(withId(R.id.username_text_view)).check(matches(isDisplayed()));
                latch2.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }

        onView(withId(R.id.username_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.username_text_view)).check(matches(withText(username)));
        
    }

    @Test
    public void loginNonexistentUserTest() {
        onView(withId(R.id.userButton)).perform(click());
        onView(withId(R.id.userName_input)).perform(typeText("]];4[6"));
        onView(withId(R.id.password_input)).perform(typeText("|:{"));
        onView(withId(R.id.login_button)).perform(click());

        onView(withText("Username or password wrong")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void signupVerifyPasswordFailTest() {
        onView(withId(R.id.userButton)).perform(click());
        onView(withId(R.id.signup_button)).perform(click());

        onView(withId(R.id.userName_input)).perform(typeText(username));
        onView(withId(R.id.password_input)).perform(typeText("123"));
        onView(withId(R.id.password_verify_input)).perform(typeText("wregerg"));
        onView(withId(R.id.signup_button)).perform(click());

        onView(withText("Please ensure the passwords match")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

}