package is.hi.hbv601g.reithi_android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import android.view.View;

import androidx.test.espresso.FailureHandler;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EspressoUtils {

    /**
     * Perform action of waiting for a specific view id.
     *
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static ViewAction waitUntilVisible(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "wait for view to be visible for " + millis + " millis";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                while (System.currentTimeMillis() < endTime) {
                    if (view.isShown()) {
                        return;
                    }
                    uiController.loopMainThreadForAtLeast(50);
                }
                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static void waitForViewToBeDisplayed(int viewId) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        long endTime = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                latch.countDown();
                break; // Exit the loop if view is displayed
            } catch (NoMatchingViewException e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for view with ID " + viewId + " to be displayed");
        }
    }

    public static void waitForViewToBeUpdated(int viewId, String expectedValue) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        long endTime = System.currentTimeMillis() + 10000; // Set end time 10 seconds from now
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                onView(withId(viewId)).check(matches(withText(expectedValue)));
                latch.countDown();
                break; // Exit the loop if view is displayed and text matches expected value
            } catch (Error e) {
                // View not displayed yet, continue waiting
            }
        }
        if (!latch.await(0, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timeout waiting for view with ID " + viewId + " to be updated");
        }
    }

}
