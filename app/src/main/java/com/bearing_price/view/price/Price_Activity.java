package com.bearing_price.view.price;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.bearing_price.data.Database;
import com.bearing_price.data.model.Price_dto;
import com.bearing_price.R;
import com.bearing_price.Recent_Adapter;

import java.util.List;

public class Price_Activity extends AppCompatActivity implements PriceNavigator{

    //    private AutoCompleteTextView auto_ed;
    private EditText ed_search;
    private List<Price_dto> price_list;
    private TextWatcher textwatcher;
    private Price_adapter adapter;
    private InputMethodManager imm;
    private ListView lv_recent, lv_search;
    private Database db;
    private Recent_Adapter recent_adapter;
    private List<Price_dto> recent_list;
    private AbsListView.OnScrollListener scroll_listener;
    private ProgressDialog pd;
    private ViewDataBinding binding;
    private PriceViewModel priceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        priceViewModel = ViewModelProviders.of(this).get(PriceViewModel.class);
        priceViewModel.setNavigator(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_price_);
//        binding.setViewModel(priceViewModel);
        binding.executePendingBindings();
        textwatcher();
        initView();

    }



    private void initView() {
        db = new Database(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        auto_ed = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ed_search = (EditText) findViewById(R.id.ed_search);
        lv_recent = (ListView) findViewById(R.id.lv_recent);
        lv_search = (ListView) findViewById(R.id.lv_search);
        recent_list = db.get_recent_search_products();
        recent_adapter = new Recent_Adapter(this, R.layout.adapter_price, recent_list);
        lv_recent.setAdapter(recent_adapter);
//        if(recent_list.size()==0)
//            imm.showSoftInputFromInputMethod(auto_ed.getWindowToken(), 0);
//        if (recent_list != null && recent_list.size() > 0) {
//            lv_recent = (ListView) findViewById(R.id.lv_recent);
//            recent_adapter = new Price_adapter(this, R.layout.adapter_price, recent_list);
//            lv_recent.setAdapter(recent_adapter);
//        } else
//            ((TextView) findViewById(R.id.tv_recent)).setVisibility(View.GONE);
//        auto_ed.setRawInputType(Configuration.KEYBOARD_QWERTY);
        price_list = db.get_search_products("");
        adapter = new Price_adapter(this, R.layout.adapter_price, price_list);
        lv_search.setAdapter(adapter);
//        auto_ed.setThreshold(1);
//        auto_ed.setAdapter(adapter);
//        auto_ed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_ed.setText(adapter.price_list.get(position).getProduct());
//                auto_ed.setSelection(auto_ed.length());
//                auto_ed.setText("");
//                db.update_recent(adapter.price_list.get(position).getId());
//                recent_list.add(0, adapter.price_list.get(position));
//                recent_adapter.notifyDataSetChanged();
//            }
//        });
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.update_recent(adapter.price_list.get(position).getId());
                recent_list.add(0, adapter.price_list.get(position));
                recent_adapter.notifyDataSetChanged();
                lv_search.setVisibility(View.GONE);
                ed_search.setText("");
            }
        });
//        lv_recent.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                Log.e("event.getAction()",""+event.getAction());
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if (imm.isAcceptingText()) {
//                        imm.hideSoftInputFromWindow(auto_ed.getWindowToken(), 0);
//                        lv_recent.requestFocus();
//                    }
//                }
//                return false;
//            }
//        });
        scroll_listener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.e("onScrollStateChanged","scrollState = "+scrollState);
//                if (scrollState == 1)
//                    imm.hideSoftInputFromWindow(auto_ed.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.e("onScroll", firstVisibleItem + " , " + visibleItemCount + " , " + totalItemCount + " , ");
                if (firstVisibleItem == 1) {
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                        lv_recent.requestFocus();
                    }
                } else if (firstVisibleItem == 0) {
                    if (!imm.isAcceptingText()) {
                        ed_search.requestFocus();
                        imm.toggleSoftInputFromWindow(ed_search.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                }
            }
        };
        ed_search.addTextChangedListener(textwatcher);
        lv_recent.setOnScrollListener(scroll_listener);
//        auto_ed.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//
//                    imm.hideSoftInputFromWindow(auto_ed.getWindowToken(), 0);
//
//                }
//                return false;
//            }
//        });
    }

    private void textwatcher() {
        textwatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
//                Log.e("onTextChanged", start + " , " + before + " , " + count);
                if (s.length() > 0) {
                    adapter.filter(ed_search.getText().toString());
                    lv_recent.setOnScrollListener(null);
                    lv_search.setVisibility(View.VISIBLE);
                    lv_search.setOnScrollListener(scroll_listener);
                } else {
                    lv_recent.setOnScrollListener(scroll_listener);
                    lv_search.setVisibility(View.GONE);
                    lv_search.setOnScrollListener(null);
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
    public void onBackPressed() {
        if (lv_search.getVisibility() == View.VISIBLE) {
            lv_recent.setOnScrollListener(scroll_listener);
            lv_search.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }
}
