package is.hi.hbv601g.reithi_android;

import android.app.Activity;

import androidx.test.espresso.IdlingResource;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

public class ActivityIdlingResource implements IdlingResource {
    private ResourceCallback resourceCallback;
    private boolean isIdle;
    private Class<? extends Activity> targetActivity;

    public ActivityIdlingResource(Class<? extends Activity> targetActivity) {
        this.targetActivity = targetActivity;
    }

    @Override
    public String getName() {
        return ActivityIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle) {
            return true;
        }

        Activity activity = ActivityLifecycleMonitorRegistry.getInstance()
                .getActivitiesInStage(Stage.RESUMED).iterator().next();
        String currentActivityName = activity.getClass().getName();

        if (currentActivityName.equals(targetActivity.getName())) {
            isIdle = true;
            if (resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
        }

        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        resourceCallback = callback;
    }
}
