package com.tekinarslan.material.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
public class LoginActivity extends BaseActivity {
    EditText et_name;//输入账号的EditText
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_name = (EditText) findViewById(R.id.et_name);
		//登录按键的监听
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(LoginActivity.this, "登录名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(name);//调用登录方法的业务逻辑
            }
        });
    }

/*
登录方法
使用了LeanCloud的SDK
*/
    public void login(String name) {
        //Jerry登录并创建在线连接
        AVIMClient jerry = AVIMClient.getInstance(name);
        jerry.open(new AVIMClientCallback() {

            @SuppressLint("WrongConstant")
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
					//登录成功的回调
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    MyApp.mClient = client;
                    startActivity(new Intent(LoginActivity.this, PlayerActivity.class));
                } else {
					//登录失败的回调
                    Toast.makeText(LoginActivity.this, "登录失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
