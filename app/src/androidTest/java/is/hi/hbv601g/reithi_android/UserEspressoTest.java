package is.hi.hbv601g.reithi_android;

import static android.content.Context.MODE_PRIVATE;
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
import static is.hi.hbv601g.reithi_android.EspressoUtils.waitUntilVisible;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import is.hi.hbv601g.reithi_android.Activities.AccountActivity;
import is.hi.hbv601g.reithi_android.Activities.LandingPageActivity;
import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.Services.UserService;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserEspressoTest {

    private UserService mUserService;

//    @Rule
//    public ActivityScenarioRule<LandingPageActivity> activityRule = new ActivityScenarioRule<>(LandingPageActivity.class);

    @Rule
    public ActivityTestRule<LandingPageActivity> activityTestRule = new ActivityTestRule<>(LandingPageActivity.class);

    @Before
    public void setUp() {
        // get a reference to the shared preferences
        SharedPreferences sharedPreferences = activityTestRule.getActivity().getSharedPreferences("MySession", MODE_PRIVATE);

// get an editor for the shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

// clear all the preferences
        editor.clear();

// commit the changes
        editor.commit();
        mUserService = new UserService(activityTestRule.getActivity());
    }

    @Test
    public void signupTest() throws InterruptedException {
        String username = "TestUser";

        // delete TestUser
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

        onView(withId(R.id.userButton)).perform(click());
        onView(withId(R.id.signup_button)).perform(click());

        onView(withId(R.id.userName_input)).perform(typeText(username));
        onView(withId(R.id.password_input)).perform(typeText("123"));
        onView(withId(R.id.password_verify_input)).perform(typeText("123"));
        onView(withId(R.id.signup_button)).perform(click());

        int viewId = R.id.username_text_view;
        IdlingRegistry.getInstance().register(new ViewIdlingResource(AccountActivity.class));
        try {
            // Wait for the view to be shown.
            onView(withId(viewId)).check(matches(isDisplayed()));
            // Perform the assertion on the view.
            onView(withId(viewId)).check(matches(withText(username)));
        } finally {
            // Unregister the idling resource.
            IdlingRegistry.getInstance().unregister(new ViewIdlingResource(AccountActivity.class));
        }

        //Thread.sleep(10000);
        //onView(isRoot()).perform(waitId(R.id.username_text_view, 15000));
//        onView(isRoot())
//                .check(matches(isDisplayed()))
//                .perform(waitId(R.id.username_text_view, 15000));

//        onView(withId(R.id.username_text_view))
//                .perform(waitUntilVisible(15000));
//
//        onView(withId(R.id.user_layout))
//                .check(matches(hasDescendant(allOf(
//                        instanceOf(TextView.class),
//                        withId(R.id.username_text_view),
//                        withText(username)
//                ))));

//        onView(withId(R.id.username_text_view))
//                .check(matches(allOf(
//                        instanceOf(TextView.class),
//                        withText(username)
//                )));

        // delete TestUser
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
    }



}