package com.tekinarslan.material.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by liujian on 2017/3/31.
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Log.d("BaseActivity", getClass().getSimpleName());
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
