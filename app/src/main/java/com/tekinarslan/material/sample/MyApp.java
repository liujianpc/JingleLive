package com.tekinarslan.material.sample;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;


public class MyApp extends Application {
    public static AVIMClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, Config.APP_ID, Config.APP_KEY);

    }


}
