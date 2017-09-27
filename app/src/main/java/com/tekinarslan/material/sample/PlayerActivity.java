package com.tekinarslan.material.sample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.LibsChecker;

import static com.tekinarslan.material.sample.PlayerFragment.roomMessageHandler;
/*
*登录成功后的界面
*有DrawerLayout、自定义的ActionBar
*ActionBarDrawerToggle、ViewPager等
*/
public class PlayerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    ViewPager pager;
    private String[] titles = new String[]{"电视", "娱乐", "体育", "户外"
            , "游戏", "电影", "音乐", "高清电影"};
    private Toolbar toolbar;
    SlidingTabLayout slidingTabLayout;
    List<PlayerFragment> fragmentList = new ArrayList<>();
    @SuppressWarnings("deprecation")
    private void initiateData() {
/*fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());
fragmentList.add(new PlayerFragment());*/
		//初始化fragmentList
        fragmentList.add(PlayerFragment.newInstance(0));
        fragmentList.add(PlayerFragment.newInstance(1));
        fragmentList.add(PlayerFragment.newInstance(2));
        fragmentList.add(PlayerFragment.newInstance(3));
        fragmentList.add(PlayerFragment.newInstance(4));
        fragmentList.add(PlayerFragment.newInstance(5));
        fragmentList.add(PlayerFragment.newInstance(6));
        fragmentList.add(PlayerFragment.newInstance(7));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
			//设置toolbar的icon
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		//fragmentList的装饰  getSupportFragmentManager()
        pager.setAdapter(new PlayFragmentAdapter(getSupportFragmentManager(), titles, fragmentList));
        pager.setOnPageChangeListener(this);
        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        //  slidingTabLayout.setOnPageChangeListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        String[] values = new String[]{
                "开直播", "换肤", "关于", "退出", "设置"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
		//抽屉item监听
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
					//关闭当前Activity，打开直播的Activity。
                        finish();
                        startActivity(new Intent(PlayerActivity.this, MainActivity.class));
                        break;

                    case 1:
						//进入换肤
                        View colorPickerView = getLayoutInflater().inflate(R.layout.color_picker_dialog, null);
                        if (colorPickerView == null) return;
						//换肤控件
                        final ColorPicker colorPicker = (ColorPicker) colorPickerView.findViewById(R.id.picker);
                        SVBar svBar = (SVBar) colorPickerView.findViewById(R.id.svbar);
                        OpacityBar opacityBar = (OpacityBar) colorPickerView.findViewById(R.id.opacitybar);
                        final TextView hexCode = (TextView) colorPickerView.findViewById(R.id.hex_code);
                        colorPicker.addSVBar(svBar);
                        colorPicker.addOpacityBar(opacityBar);
                        colorPicker.setOldCenterColor(R.color.material_deep_teal_500);
                        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int color) {
                                String hexColor = Integer.toHexString(color).toUpperCase();
                                hexCode.setText("#" + hexColor);
                            }
                        });
						//对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                        builder.setView(colorPickerView);
                        builder.setTitle("选择皮肤颜色");
                        builder.setCancelable(true);
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @SuppressWarnings("WrongConstant")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                int color = colorPicker.getColor();
                                mDrawerList.setBackgroundColor(color);
                                toolbar.setBackgroundColor(color);
                                slidingTabLayout.setBackgroundColor(color);
                                mDrawerLayout.closeDrawer(Gravity.START);
                            }
                        });
                        builder.create().show();

                        /*mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mDrawerLayout.closeDrawer(Gravity.START);*/
                        break;
                    case 2:
					//打开关于软件Activity
                        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);			
                        startActivity(intent);
                        break;
                    case 3:
					//退出整个程序
                        finish();
                        ActivityCollector.finishAllActivity();
                        break;
                    case 4:
					//打开设置SettingsActivity
                        Intent intent1 = new Intent(PlayerActivity.this, SettingsActivity.class);
                        startActivity(intent1);
                        break;
                }

            }
        });

     /*   if (fragmentList.get(0).mVideoView != null) {
            fragmentList.get(0).mVideoView.start();
        }
*/
        pager.setCurrentItem(0);
        // fragmentList.add(1,null);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_sample);
        initiateData();//调用布局数据初始化方法

    }

    @SuppressWarnings("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }*/

//onResume状态绑定消息处理器
    @Override
    protected void onResume() {
        super.onResume();
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, roomMessageHandler);
    }

	//onPause状态解绑消息处理器
    @Override
    protected void onPause() {
        super.onPause();
        AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, roomMessageHandler);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      /*  Config.lastItem = Config.currentItem;
        Config.currentItem = position;
        if (fragmentList.get(Config.lastItem) != null) {
            if (fragmentList.get(Config.lastItem).mVideoView.isPlaying()) {
                fragmentList.get(Config.lastItem).mVideoView.pause();
            }

        }*/

        //fragmentList.get(Config.currentItem).mVideoView.setVideoPath(paths[Config.currentItem]);
        /*fragmentList.get(Config.currentItem).mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    fragmentList.get(Config.currentItem).layout_loading.setVisibility(View.VISIBLE);
                    android.util.Log.i("zzz", "onStart");
                    Log.d("zzz", "onStart");

                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                    android.util.Log.i("zzz", "onEnd");
                    Log.d("zzz", "onEnd");
                    fragmentList.get(Config.currentItem).layout_loading.setVisibility(View.GONE);
                    //   mp.start();
                    fragmentList.get(Config.currentItem).mVideoView.start();
                }
                return true;
            }
        });
        fragmentList.get(Config.currentItem).mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (!mp.isPlaying()) {
                    fragmentList.get(Config.currentItem).layout_loading.setVisibility(View.VISIBLE);
                    fragmentList.get(Config.currentItem).tv_present.setText("正在缓冲" + percent + "%");
                    Log.d("buffering", "buffering now");
                } else {
                    fragmentList.get(Config.currentItem).layout_loading.setVisibility(View.GONE);
                }
            }
        });

        fragmentList.get(Config.currentItem).mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                android.util.Log.i("zzz", "onError what=" + what + " extra=" + extra);
                Log.d("error", "error happened");
                fragmentList.get(Config.currentItem).tv_present.setText("加载失败");
                return true;
            }
        });

        fragmentList.get(Config.currentItem).mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });*/
        // fragmentList.get(Config.currentItem).mVideoView.start();

        android.util.Log.d("OnPageSelect", "enter in");
        android.util.Log.d("OnPageSelect", "enter in");
        android.util.Log.d("OnPageSelect", "enter in");

        /*if (mMediaControllerList.get(lastItem) != null) {
            if (mMediaControllerList.get(lastItem).isShowing()) {
                mMediaControllerList.get(lastItem)
                        .setVisibility(View.INVISIBLE);
            }
        }*/

      /*  if (fragmentList.get(currentItem).mVideoView != null) {
            // 如果页面第一次加载
            // if (mIsPageFirstAvaliable.get(currentItem)) {
            // mIsPlaying.put(currentItem, true);
            // mIsPageFirstAvaliable.put(currentItem, false);
            // } else {
            // 不是第一次加载跳到当前播放进度
            mVideoViewList.get(currentItem).seekTo(
                    mCurrentPositions.get(currentItem));
            // }*/
          /*  if (mIsPlaying.get(currentItem)) {
                mVideoViewList.get(currentItem).start();
            }
            mMediaControllerList.get(currentItem).show(1000);*/


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("onPageScrol", "entered");
    }


    /**
     * 添加横竖屏监听，隐藏图形的菜单控件
     *
     */
   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   @SuppressWarnings("WrongConstant")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // // 设置全屏
            // // 设置全屏
          /* getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
            toolbar.setVisibility(View.GONE);
            slidingTabLayout.setVisibility(View.GONE);

          //View v  = getWindow().getDecorView();
           /*      int i = v.getSystemUiVisibility();
                if (i == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
                    v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (i == View.SYSTEM_UI_FLAG_VISIBLE) {
                    v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                } else if (i == View.SYSTEM_UI_FLAG_LOW_PROFILE) {
                    v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            }*/
           /* v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/



        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            /*WindowManager.LayoutParams attrs =getWindow()
                    .getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);*/
            toolbar.setVisibility(View.VISIBLE);
            slidingTabLayout.setVisibility(View.VISIBLE);

        }
    }


}
