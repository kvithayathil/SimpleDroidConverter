package com.jedikv.simpleconverter.ui.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.ui.adapters.CurrencyPickerAdapter;
import com.jedikv.simpleconverter.utils.AndroidUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Kurian on 13/05/2015.
 */
public class CurrencyPickerActivity extends BaseActivity {

    public static final int REQUEST_CODE_ADD_CURRENCY = 100;
    public static final int REQUEST_CODE_CHANGE_CURRENCY = 200;

    public static final int RESULT_CODE_ADD_CURRENCY_SUCCESS = 101;
    public static final int RESULT_CODE_CHANGE_CURRENCY_SUCCESS = 201;

    public static final String EXTRA_CURRENCY_LIST = "extra_currency_list";
    // The elevation of the toolbar when content is scrolled behind
    private static final float TOOLBAR_ELEVATION = 14f;
    @InjectView(R.id.list)
    RecyclerView recyclerView;
    @InjectView(R.id.toolbar)
    Toolbar toolBar;

    private CurrencyPickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_picker);
        ButterKnife.inject(this);
        setSupportActionBar(toolBar);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // We need to detect scrolling changes in the RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            // Keeps track of the overall vertical offset in the list
            int verticalOffset;

            // Determines the scroll UP/DOWN direction
            boolean scrollingUp;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {
                        if (verticalOffset > toolBar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    } else {
                        if (toolBar.getTranslationY() < toolBar.getHeight() * -0.6 && verticalOffset > toolBar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    }
                }
            }

            @Override
            public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                verticalOffset += dy;
                scrollingUp = dy > 0;
                int toolbarYOffset = (int) (dy - toolBar.getTranslationY());
                toolBar.animate().cancel();
                if (scrollingUp) {
                    if (toolbarYOffset < toolBar.getHeight()) {
                        if (verticalOffset > toolBar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        toolBar.setTranslationY(-toolbarYOffset);
                    } else {
                        toolbarSetElevation(0);
                        toolBar.setTranslationY(-toolBar.getHeight());
                    }
                } else {
                    if (toolbarYOffset < 0) {
                        if (verticalOffset <= 0) {
                            toolbarSetElevation(0);
                        }
                        toolBar.setTranslationY(0);
                    } else {
                        if (verticalOffset > toolBar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        toolBar.setTranslationY(-toolbarYOffset);
                    }
                }
            }
        });



        if(getIntent().getExtras() != null) {

            mAdapter = new CurrencyPickerAdapter(this, getIntent().getExtras().getStringArrayList(EXTRA_CURRENCY_LIST));


            recyclerView.setAdapter(mAdapter);
        }


    }

    /**
     * Raise the toolbar elevation when animating over the list
     * @param elevation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        // setElevation() only works on Lollipop
        if (AndroidUtils.isLollipop()) {
            toolBar.setElevation(elevation);
        }
    }

    /**
     * For use when scrolling down on the list
     * @param verticalOffset
     */
    private void toolbarAnimateShow(final int verticalOffset) {
        toolBar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbarSetElevation(verticalOffset == 0 ? 0 : TOOLBAR_ELEVATION);
                    }
                });
    }

    /**
     * For use when scrolling up on the list
     */
    private void toolbarAnimateHide() {
        toolBar.animate()
                .translationY(-toolBar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbarSetElevation(0);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_currency_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
