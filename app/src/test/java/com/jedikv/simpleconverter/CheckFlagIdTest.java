package com.jedikv.simpleconverter;

import android.content.Context;
import android.test.mock.MockContext;

import com.jedikv.simpleconverter.ui.activities.MainActivity;
import com.jedikv.simpleconverter.utils.AndroidUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.easymock.EasyMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by Kurian on 31/05/2015.
 */
@RunWith(PowerMockRunner.class)
public class CheckFlagIdTest {

    private int flagId = R.drawable.gb_;

    Context mMockContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMockContext = createMock(Context.class);
        EasyMock.replay(mMockContext);
    }

    @Test
    public void readFlagId() {

        mock(AndroidUtils.class);

        int resultId =AndroidUtils.getDrawableResIdByCountryCode(mMockContext, "GB");

        assertThat(resultId, is(flagId));
    }
}
