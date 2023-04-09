package is.hi.hbv601g.reithi_android;

import android.app.Activity;
import android.view.View;

import androidx.test.espresso.IdlingResource;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

public class ViewIdlingResource implements IdlingResource {

    private final int viewId;
    private final long waitTime;
    private final long startTime;
    private ResourceCallback resourceCallback;

    public ViewIdlingResource(int viewId, long waitTime) {
        this.viewId = viewId;
        this.waitTime = waitTime;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return ViewIdlingResource.class.getName() + ":" + viewId;
    }

    @Override
    public boolean isIdleNow() {
        Activity activity = ActivityLifecycleMonitorRegistry.getInstance()
                .getActivitiesInStage(Stage.RESUMED).iterator().next();

        View view = activity.findViewById(viewId);

        boolean idle = view != null && view.isShown();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle || System.currentTimeMillis() - startTime >= waitTime;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}

