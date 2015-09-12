package com.jedikv.simpleconverter;

import org.junit.Before;
import org.robolectric.shadows.ShadowApplication;

import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.util.ReflectionHelpers.setStaticField;

/**
 * Created by Kurian on 22/05/2015.
 *
 * @see <a href="https://github.com/jaredsburrows/AndroidGradleTemplate/blob/master/src/test/java/burrows/apps/example/template/test/TestBase.java">jaredsburrows/AndroidGradleTemplate</a>
 *
 */
public class TestBase {

    @Before
    public void setUp() {
        //ShadowApplication shadowApplication = shadowOf(application);



        //Use the debug configuration
        setStaticField(BuildConfig.class, "DEBUG", false);
    }


}
