package is.hi.hbv601g.reithi_android;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.uiautomator.UiDevice;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;

public class DisableAnimationsRule implements TestRule {

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                disableAnimations();
                try {
                    base.evaluate();
                } finally {
                    enableAnimations();
                }
            }
        };
    }

    private void disableAnimations() throws IOException {
        UiDevice.getInstance(getInstrumentation()).executeShellCommand("settings put global window_animation_scale 0.0");
        UiDevice.getInstance(getInstrumentation()).executeShellCommand("settings put global transition_animation_scale 0.0");
        UiDevice.getInstance(getInstrumentation()).executeShellCommand("settings put global animator_duration_scale 0.0");
    }

    private void enableAnimations() throws IOException {
        UiDevice.getInstance(getInstrumentation()).executeShellCommand("settings put global window_animation_scale 1.0");
        UiDevice.getInstance(getInstrumentation()).executeShellCommand("settings put global transition_animation_scale 1.0");
        UiDevice.getInstance(getInstrumentation()).executeShellCommand("settings put global animator_duration_scale 1.0");
    }
}

