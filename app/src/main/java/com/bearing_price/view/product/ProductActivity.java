package com.bearing_price.view.product;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bearing_price.R;
import com.bearing_price.Recent_Adapter;
import com.bearing_price.data.model.Price_dto;
import com.bearing_price.view.price.Price_adapter;

import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductNavigator {
    private EditText ed_search;
    private Product_adapter adapter;
    private InputMethodManager imm;
    private ListView lv_recent, lv_search;
    private Recent_Adapter recent_adapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.setNavigator(this);
        initView();
        initObserver();
        initListener();
    }

    private void initListener() {
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productViewModel.update_recentTimes(adapter.getItem(position));
            }
        });


        ed_search.addTextChangedListener(textwatcher());
        lv_recent.setOnScrollListener(scrollListener());
    }

    private void initObserver() {
        Observer<List<Price_dto>> recentlistObserver = new Observer<List<Price_dto>>() {

            @Override
            public void onChanged(@Nullable List<Price_dto> price_dtoList) {
                if (price_dtoList != null && price_dtoList.size() > 0)
                    setRecentAdapter(price_dtoList);

            }
        };

        productViewModel.getRecentList().observe(this, recentlistObserver);

        Observer<List<Price_dto>> allListObserver = new Observer<List<Price_dto>>() {

            @Override
            public void onChanged(@Nullable List<Price_dto> price_dtoList) {
                if (price_dtoList != null && price_dtoList.size() > 0)
                    setAdapter(price_dtoList);
            }
        };

        productViewModel.getAllProductsList("").observe(this, allListObserver);
    }

    private void initView() {
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        ed_search = (EditText) findViewById(R.id.ed_search);
        lv_recent = (ListView) findViewById(R.id.lv_recent);
        lv_search = (ListView) findViewById(R.id.lv_search);


    }

    private AbsListView.OnScrollListener scrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 1) {
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                        lv_recent.requestFocus();
                    }
                } else if (firstVisibleItem == 0) {
                    if (!imm.isAcceptingText()) {
                        ed_search.requestFocus();
                        imm.toggleSoftInputFromWindow(ed_search.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        };
    }

    private TextWatcher textwatcher() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
//                Log.e("onTextChanged", start + " , " + before + " , " + count);
                if (s.length() > 0) {
                    productViewModel.getAllProductsList(ed_search.getText().toString());
//                    adapter.filter(ed_search.getText().toString());
                    lv_search.setVisibility(View.VISIBLE);
//                    lv_recent.setOnScrollListener(null);
//                    lv_search.setOnScrollListener(scroll_listener);
                } else {
                    lv_search.setVisibility(View.GONE);
//                    lv_recent.setOnScrollListener(scroll_listener);
//                    lv_search.setOnScrollListener(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        };
    }

    @Override
    public void setAdapter(List<Price_dto> list) {
        if (adapter == null) {
            adapter = new Product_adapter(this, R.layout.adapter_price, list);
            lv_search.setAdapter(adapter);
        } else
            adapter.setData(list);


    }

    @Override
    public void setRecentAdapter(List<Price_dto> recent_list) {
        if (adapter == null) {
            recent_adapter = new Recent_Adapter(this, R.layout.adapter_price, recent_list);
            lv_recent.setAdapter(recent_adapter);
        } else
            recent_adapter.setData(recent_list);

        lv_search.setVisibility(View.GONE);
        ed_search.setText("");
    }

    @Override
    public void emptyListError() {

    }
}
