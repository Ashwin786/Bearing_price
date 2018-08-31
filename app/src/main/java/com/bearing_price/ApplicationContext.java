package com.bearing_price;

import android.app.Application;
import android.content.Context;

/**
 * Created by user1 on 28/8/18.
 */
public class ApplicationContext extends Application {

    private static ApplicationContext instance;

    public ApplicationContext() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}