package com.tekinarslan.material.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.tekinarslan.material.sample.view.BarrageView;
import com.tekinarslan.material.sample.view.MyVideoView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;

/**
 * Created by liujian on 2017/4/5.
 */

public class PlayerFragment extends Fragment implements View.OnClickListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        View.OnKeyListener {

    public View rootView;
    protected boolean isInit = false;
    protected boolean isLoad = false;
    // protected final String TAG = "LazyLoadFragment";
    public static RoomMessageHandler roomMessageHandler;
    public MyVideoView mVideoView;
    String[] paths = {Config.VIDEO_URL, "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8", "http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8",
            "http://ivi.bupt.edu.cn/hls/lytv.m3u8", "http://ivi.bupt.edu.cn/hls/cctv10.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv15.m3u8", "http://ivi.bupt.edu.cn/hls/chchd.m3u8"};
    EditText et_send;
    BarrageView barrageView;
    ScrollView ll_room;
    View layout_video, layout_loading;
    TextView tv_present;
    ImageButton sendButton;
    ImageButton fullButton, danMuButton;
    boolean danMu;
    boolean isFull = false;
    private ImageButton playPasueBtn;


    private static final String ARG_POSITION = "position";

    private int position;

    public static PlayerFragment newInstance(int position) {
        PlayerFragment f = new PlayerFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("onCreateView", "onCreateView");
        roomMessageHandler = new RoomMessageHandler();
        position = getArguments().getInt(ARG_POSITION);
        rootView = inflater.inflate(R.layout.page, container, false);

        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();

        //   mVideoView.setVideoPath(paths[Config.currentItem]);
        //        if (position == 1) Config.currentItem++;
       /* if (position == Config.currentItem){

            mVideoView.setVideoPath(paths[position]);
            mVideoView.setMediaController(mediaController);
            mVideoView.requestFocus();
            mVideoView.setOnInfoListener(this);
            mVideoView.setOnBufferingUpdateListener(this);
            mVideoView.setOnPreparedListener(this);
            mVideoView.setOnErrorListener(this);
       }*/


        return rootView;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    public void lazyLoad() {

        mVideoView = (MyVideoView) rootView.findViewById(R.id.vitamio_videoView0);
        et_send = (EditText) rootView.findViewById(R.id.et_send0);
        barrageView = (BarrageView) rootView.findViewById(R.id.containerView0);
        ll_room = (ScrollView) rootView.findViewById(R.id.ll_room0);
        layout_video = rootView.findViewById(R.id.layout_video0);
        layout_loading = rootView.findViewById(R.id.layout_loading0);
        tv_present = (TextView) rootView.findViewById(R.id.tv_present0);
        sendButton = (ImageButton) rootView.findViewById(R.id.send_button0);
        fullButton = (ImageButton) rootView.findViewById(R.id.btn_fullscreen0);
        danMuButton = (ImageButton) rootView.findViewById(R.id.btn_danmu0);
        playPasueBtn = (ImageButton) rootView.findViewById(R.id.mediacontroller_play_pause0);
        danMu = true;
        MediaController mediaController = (MediaController) rootView.findViewById(R.id.mediacontroller0);
        mediaController.setEnabled(true);

        //  android.widget.MediaController mediaController = new android.widget.MediaController(getContext(),true);
        et_send.setOnKeyListener(this);
        danMuButton.setOnClickListener(this);
        fullButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        mVideoView.setVideoPath(paths[position]);
        mVideoView.setMediaController(mediaController);
        mVideoView.requestFocus();
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        playPasueBtn.setOnClickListener(this);
        join();
    }

    ;

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    public void stopLoad() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        /*mVideoView.setMediaController(mediaController);
        mVideoView.requestFocus();
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);*/
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;

    }

    private void join() {
        MyApp.mClient.open(new AVIMClientCallback() {

            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    //登录成功
                    Config.conv = client.getConversation(Config.CONVERSATION_ID);
                    // conv = client.getConversation(Config.CONVERSATION_ID);
                    Config.conv.join(new AVIMConversationCallback() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void done(AVIMException e) {
                            if (e == null) {
                                //加入成功
                                Toast.makeText(getContext(), "加入聊天室成功", Toast.LENGTH_SHORT).show();
                                et_send.setEnabled(true);
                            } else {
                                Toast.makeText(getContext(), "加入聊天室失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                et_send.setEnabled(false);
                                android.util.Log.i("zzz", "加入聊天室失败 :" + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    class RoomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            if (message instanceof AVIMTextMessage) {
                String info = ((AVIMTextMessage) message).getText();
                //添加消息到屏幕
                addMsg(info);
            }
        }

    }

    @SuppressWarnings("ResourceType")
    private void addMsg(String msg) {

        TextView textView = new TextView(getContext());
        textView.setText(msg);
        textView.setTextColor(Color.parseColor("#80000000"));
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 10, 5, 10);
        textView.setLayoutParams(params);
        ll_room.addView(textView, 0);//添加聊天记录
        barrageView.addMessage(msg);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_danmu0:

                if (danMu) {
                    danMu = false;
                    barrageView.setVisibility(View.INVISIBLE);
                    danMuButton.setSelected(true);
                    //danMuButton.setBackground(getResources().getDrawable(io.vov.vitamio.R.drawable.danmu_off));

                } else {
                    danMu = true;
                    barrageView.setVisibility(View.VISIBLE);
                    danMuButton.setSelected(false);
                    //danMuButton.setBackground(getResources().getDrawable(io.vov.vitamio.R.drawable.danmu_on));
                }

                break;

            case R.id.btn_fullscreen0:
                if (isFull){
                    fullButton.setSelected(false);
                }else {
                    fullButton.setSelected(true);
                }
                fullScreen();
                break;
            case R.id.send_button0:
                sendMsg();
                // Toast.makeText(getContext(), "send message", Toast.LENGTH_SHORT).show();
                et_send.setText("");
                break;
            case R.id.mediacontroller_play_pause0:
                if (mVideoView.isPlaying()) {
                    playPasueBtn.setSelected(true);
                    mVideoView.pause();
                } else {
                    playPasueBtn.setSelected(false);
                    mVideoView.start();
                }

                break;

            default:

                break;

        }

    }


    @SuppressLint("WrongConstant")
    private void sendMsg() {

        final String msg = et_send.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Config.conv != null) {
            AVIMTextMessage message = new AVIMTextMessage();
            message.setText(msg);
            Config.conv.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e == null) {
                        et_send.setText("");
                        addMsg(msg);
                    } else {
                        Toast.makeText(getContext(), "发送失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void fullScreen() {
        if (isScreenOriatationPortrait(getContext())) {// 当屏幕是竖屏时
            //full(true);//此处可以设置为隐去，无需再次调用全屏函数，因为在onConfigureChanged函数中会有一个全屏操作。
            // 点击后变横屏
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置当前activity为横屏
            // 当横屏时 把除了视频以外的都隐藏
            //隐藏其他组件的代码
            ll_room.setVisibility(View.GONE);
            et_send.setVisibility(View.GONE);
            Config.show = false;
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            layout_video.setLayoutParams(new LinearLayout.LayoutParams(height, width));
            mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(height, width));
            isFull = true;


        } else {
            full(false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置当前activity为竖屏
            //显示其他组件
            ll_room.setVisibility(View.VISIBLE);
            et_send.setVisibility(View.VISIBLE);
            int width = getResources().getDisplayMetrics().heightPixels;
            int height = (int) (width * 9.0 / 16);
            layout_video.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
            isFull = false;

        }

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            sendMsg();
            return true;
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (!mp.isPlaying()) {
            layout_loading.setVisibility(View.VISIBLE);
            tv_present.setText("正在缓冲" + percent + "%");
            Log.d("buffering", "buffering now");
        } else {
            layout_loading.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        android.util.Log.i("zzz", "onError what=" + what + " extra=" + extra);
        Log.d("error", "error happened");
        tv_present.setText("加载失败");
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            layout_loading.setVisibility(View.VISIBLE);
            android.util.Log.i("zzz", "onStart");
            Log.d("zzz", "onStart");

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
            android.util.Log.i("zzz", "onEnd");
            Log.d("zzz", "onEnd");
            layout_loading.setVisibility(View.GONE);
            //   mp.start();
            mVideoView.start();
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setPlaybackSpeed(1.0f);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
//		System.out.println("TwoFragment onCreate");
        super.onCreate(savedInstanceState);
        Log.d("Oncreate", "Oncreate");
    }


    @Override
    public void onPause() {
//		System.out.println("TwoFragment  onPause");
        // TODO Auto-generated method stub
        super.onPause();
        Log.d("Onpause", "Onpause");
    }

    @Override
    public void onResume() {
//		System.out.println("TwoFragment  onResume");
        // TODO Auto-generated method stub
        super.onResume();
        Log.d("Onresume", "Onresume");
    }

    @Override
    public void onDestroy() {
//		System.out.println("TwoFragment  onDestroy");
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d("Ondestory", "Ondestory");
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }


    @Override
    public void onStop() {
//		System.out.println("TwoFragment  onStop");
        // TODO Auto-generated method stub
        super.onStop();
        Log.d("Onstop", "Onstop");
    }

    @Override
    public void onStart() {
//		System.out.println("TwoFragment  onStart");
        // TODO Auto-generated method stub
        super.onStart();
        Log.d("OnStart", "OnStart");
    }

    //动态隐藏状态栏
    private void full(boolean enable) {
        if (enable) {
           /* WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getActivity().getWindow().setAttributes(lp);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
            Window window = getActivity().getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
           /* View decorView  = getActivity().getWindow().getDecorView();
            int uiParams =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiParams);*/
        } else {
         /*   WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().setAttributes(attr);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
           /* View decorView  = getActivity().getWindow().getDecorView();
            int uiParams =  View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiParams);
            decorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"you click it", Toast.LENGTH_SHORT).show();                }
            });*/
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

    }
}
