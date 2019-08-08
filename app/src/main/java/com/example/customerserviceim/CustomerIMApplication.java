package com.example.customerserviceim;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

public class CustomerIMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
        options.setUseFCM(false);
        if (EaseUI.getInstance().init(this, options)) {
            EMClient.getInstance().setDebugMode(true);
        }
    }
}
