package com.example.customerserviceim;

import android.app.Application;

public class CustomerIMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化环信SDK
        CustomerIMHelper.getInstance().init(this);

    }
}
