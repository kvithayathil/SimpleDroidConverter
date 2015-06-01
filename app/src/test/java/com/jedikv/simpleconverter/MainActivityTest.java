package com.jedikv.simpleconverter;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.jedikv.simpleconverter.ui.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.robolectric.Robolectric.buildActivity;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest extends TestBase {

    MainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();

    }

    @Test
    public void test_notNull() {

        assertThat(mainActivity, not(nullValue()));
    }

}
