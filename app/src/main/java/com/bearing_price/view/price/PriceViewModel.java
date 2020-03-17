package com.bearing_price.view.price;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;


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
