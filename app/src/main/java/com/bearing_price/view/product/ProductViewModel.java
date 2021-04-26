package com.bearing_price.view.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bearing_price.data.localDb.DbRepository;
import com.bearing_price.data.model.Price_dto;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductNavigator mNavigator;
    MutableLiveData<List<Price_dto>> recentData = new MutableLiveData<>();
    MutableLiveData<List<Price_dto>> searchData = new MutableLiveData<>();

    public MutableLiveData<List<Price_dto>> getRecentList() {
        List<Price_dto> list = DbRepository.getInstance().get_recent_search_products();
        recentData.setValue(list);
        return recentData;
    }

    public ProductNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(ProductNavigator navigator) {
        this.mNavigator = navigator;
    }

    public void update_recentTimes(Price_dto dto) {
        DbRepository.getInstance().update_RecentProduct(dto.getId());
        List<Price_dto> tempList = recentData.getValue();
        tempList.add(0, dto);
        recentData.setValue(tempList);
    }

    public LiveData<List<Price_dto>> getAllProductsList(String text) {
        List<Price_dto> list = DbRepository.getInstance().get_search_products(text);

        searchData.setValue(list);
        return searchData;
    }
}
