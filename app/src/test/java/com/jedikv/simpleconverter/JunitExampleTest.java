package com.jedikv.simpleconverter;


import android.os.Build;

import com.jedikv.simpleconverter.ui.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Kurian on 22/05/2015.
 */

public class JunitExampleTest {

    int testOne;

    @Before
    public void setUp() {
        testOne = 1;
    }

    @Test
    public void testOne() {
        assertThat(testOne, is(1));
    }
}
