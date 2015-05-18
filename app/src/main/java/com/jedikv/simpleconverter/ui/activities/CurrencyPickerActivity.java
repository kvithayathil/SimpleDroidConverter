package com.jedikv.simpleconverter.ui.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;


import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.AddCurrencyEvent;
import com.jedikv.simpleconverter.ui.adapters.CurrencyPickerAdapter;
import com.jedikv.simpleconverter.utils.AndroidUtils;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;
import timber.log.Timber;

/**
 * Created by Kurian on 13/05/2015.
 */
public class CurrencyPickerActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    public static final int REQUEST_CODE_ADD_CURRENCY = 1000;
    public static final int REQUEST_CODE_CHANGE_CURRENCY = 2000;

    public static final int RESULT_CODE_SUCCESS = 1001;

    public static final String EXTRA_CURRENCY_LIST = "extra_currency_list";

    public static final String EXTRA_SELECTED_CURRENCY_CODE = "extra_selected_currency_code";

    // The elevation of the toolbar when content is scrolled behind
    private static final float TOOLBAR_ELEVATION = 14f;
    @InjectView(R.id.list)
    ObservableRecyclerView recyclerView;
    @InjectView(R.id.toolbar)
    Toolbar toolBar;

    private SearchView searchView;

    private CurrencyPickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_picker);
        ButterKnife.inject(this);
        setSupportActionBar(toolBar);

        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if(getIntent().getExtras() != null) {

            mAdapter = new CurrencyPickerAdapter(this, getIntent().getExtras().getStringArrayList(EXTRA_CURRENCY_LIST));

            recyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_currency_picker, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) CurrencyPickerActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if(searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        }

        if(searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(CurrencyPickerActivity.this.getComponentName()));

        }

        return super.onCreateOptionsMenu(menu);
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

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(toolBar) == 0;
    }


    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(toolBar) == -toolBar.getHeight();
    }


    private void showToolbar() {
        moveToolbar(0);
    }


    private void hideToolbar() {
        moveToolbar(-toolBar.getHeight());
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        Timber.d("onUpOrCancelMotionEvent: " + scrollState);

        if(scrollState == ScrollState.UP) {
            if(toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {

            if(toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(toolBar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolBar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolBar, translationY);
                ViewHelper.setTranslationY((View) recyclerView, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) recyclerView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) recyclerView).requestLayout();
            }
        });
        animator.start();
    }

    @Subscribe
    public void onCurrencyPicked(AddCurrencyEvent event) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED_CURRENCY_CODE, event.getCurrencyCode());
        setResult(RESULT_CODE_SUCCESS, resultIntent);
        finish();
    }
}
