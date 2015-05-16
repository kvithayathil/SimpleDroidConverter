package com.jedikv.simpleconverter.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.ui.adapters.CurrencyPickerAdapter;

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

    @InjectView(R.id.list)
    RecyclerView recyclerView;
    @InjectView(R.id.autocomp_search)
    AppCompatAutoCompleteTextView autoCompleteTextView;

    private CurrencyPickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_picker);
        ButterKnife.inject(this);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        if(getIntent().getExtras() != null) {

            mAdapter = new CurrencyPickerAdapter(this, getIntent().getExtras().getStringArrayList(EXTRA_CURRENCY_LIST));


            recyclerView.setAdapter(mAdapter);
        }


    }


}
