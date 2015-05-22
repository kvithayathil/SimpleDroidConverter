package com.jedikv.simpleconverter;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.jedikv.simpleconverter.ui.activities.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.robolectric.Robolectric.buildActivity;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest extends TestBase {

    @Test
    public void test_notNull() {

        MainActivity mainActivity = buildActivity(MainActivity.class)
                .create()
                .start()
                .resume()
                .pause()
                .destroy()
                .visible()
                .get();
        assertThat(mainActivity, not(nullValue()));
    }

}
