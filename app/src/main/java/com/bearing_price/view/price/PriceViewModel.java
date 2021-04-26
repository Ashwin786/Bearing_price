package com.bearing_price.view.price;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class PriceViewModel extends AndroidViewModel {

    private PriceNavigator mNavigator;

    public PriceViewModel(@NonNull Application application) {
        super(application);
    }

    public PriceNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(PriceNavigator navigator) {
        this.mNavigator = navigator;
    }
}
