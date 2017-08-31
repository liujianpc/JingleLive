package com.tekinarslan.material.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.tekinarslan.material.sample.utils.NetworkUtils;
import java.util.TimerTask;
/**
 * 首先检查网络状态，如果网络连接正常，则利用延时任务打LoginActivity登录界面
 * 延时的那段时间显示SplashActivity的欢迎页
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!checkNetwork()) {
            return;
        }
        new Handler().postDelayed(new TimerTask() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);//主线程上的延时任务
         checkUpdate();//更新检查
    }
	/*
	检查网络状态的方法
	*/
    private boolean checkNetwork() {
        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "请检查网络状态！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
	/*
	检查更新的方法
	*/
    private void checkUpdate() {
        Toast.makeText(this, "检查新版本...", Toast.LENGTH_SHORT).show();
    }
}
