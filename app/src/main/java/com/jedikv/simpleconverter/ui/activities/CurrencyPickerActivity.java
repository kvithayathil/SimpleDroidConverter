package com.jedikv.simpleconverter.ui.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.AddCurrencyEvent;
import com.jedikv.simpleconverter.ui.adapters.CurrencyPickerAdapter;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import converter_db.CurrencyEntity;
import timber.log.Timber;

/**
 * Created by Kurian on 13/05/2015.
 */
public class CurrencyPickerActivity extends BaseActivity {

    public static final int REQUEST_CODE_ADD_CURRENCY = 1000;
    public static final int REQUEST_CODE_CHANGE_CURRENCY = 2000;

    public static final int RESULT_CODE_SUCCESS = 1001;

    public static final String EXTRA_CURRENCY_LIST = "extra_currency_list";

    public static final String EXTRA_SELECTED_CURRENCY_CODE = "extra_selected_currency_code";

    // The elevation of the toolbar when content is scrolled behind
    private static final float TOOLBAR_ELEVATION = 14f;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolBar;

    private SearchView searchView;

    private CurrencyPickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_picker);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recyclerView.setScrollViewCallbacks(this);
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
            searchView.setQueryHint(getString(R.string.hint_search_currency));
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


    @Subscribe
    public void onCurrencyPicked(AddCurrencyEvent event) {

        CurrencyEntity currencyEntity = event.getCurrency();

        Timber.d("Currency Code: " + currencyEntity.getCode() + " Symbol: " + currencyEntity.getName());

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED_CURRENCY_CODE, currencyEntity.getNumericCode());
        setResult(RESULT_CODE_SUCCESS, resultIntent);
        finish();
    }
}
