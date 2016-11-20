package com.jedikv.simpleconverter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by KV_87 on 20/11/2016.
 */
@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.jedikv.simpleconverter", appContext.getPackageName());
    }
}
