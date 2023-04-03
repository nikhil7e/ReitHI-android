package is.hi.hbv601g.reithi_android;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;

public class FragmentLoadingIdlingResource implements IdlingResource {
    private final FragmentManager fragmentManager;
    private final Fragment fragment;
    private volatile ResourceCallback resourceCallback;

    public FragmentLoadingIdlingResource(FragmentManager fragmentManager, Fragment fragment) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
    }

    @Override
    public String getName() {
        return FragmentLoadingIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = isFragmentIdle();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    private boolean isFragmentIdle() {
        boolean idle = fragment.getView() != null && fragment.getView().getWindowToken() != null;
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }
}

