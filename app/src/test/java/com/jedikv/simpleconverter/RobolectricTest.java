package com.jedikv.simpleconverter;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;

import com.jedikv.simpleconverter.ui.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

// Static imports for assertion methods
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by KV_87 on 12/09/15.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class RobolectricTest  {

    private ActivityController<MainActivity> controller;
    private MainActivity activity;

    // @Before => JUnit 4 annotation that specifies this method should run before each test is run
    // Useful to do setup for objects that are needed in the test
    @Before
    public void setup() {
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        controller = Robolectric.buildActivity(MainActivity.class);
        activity = controller.get();

        //activity = Robolectric.setupActivity(MainActivity.class);
    }

    // @Test => JUnit 4 annotation specifying this is a test to be run
    // The test simply checks that our TextView exists and has the text "Hello world!"
    @Test
    public void validateTextViewContent() {

        controller.create().start().resume();

        //FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        assertNull("Activity could not be found", activity);
    }


    // @After => JUnit 4 annotation that specifies this method should be run after each test
    @After
    public void tearDown() {
        // Destroy activity after every test

        controller
                .pause()
                .stop()
                .destroy();

    }
}

