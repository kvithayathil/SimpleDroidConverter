package com.jedikv.simpleconverter;

import android.content.Context;

import com.jedikv.simpleconverter.utils.AndroidUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.MockPolicy;

/**
 * Created by Kurian on 31/05/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckFlagIdTest {

    private static final int flagId = R.drawable.gb_;

    @Mock
    Context mMockContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void readFlagId() {


        int resultId =AndroidUtils.getDrawableResIdByCountryCode(mMockContext, "GB");

        assertThat(resultId, is(flagId));
    }
}
