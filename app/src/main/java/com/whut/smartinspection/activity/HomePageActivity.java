package com.whut.smartinspection.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.todddavies.components.progressbar.ProgressWheel;
import com.whut.smartinspection.R;
import com.whut.smartinspection.services.HttpService;
import com.whut.smartinspection.utils.StatusBarUtils;
import com.whut.smartinspection.widgets.LoopSlidingView;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fortuner on 2017/12/14.
 */

public class HomePageActivity extends SwipeBackActivity {
    @BindView(R.id.lsv_home_page)
    LoopSlidingView loopSlidingView;
    private MyReceiver receiver=null;
    ProgressWheel pw = null;
    @BindView(R.id.ll_list)
    LinearLayout llList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

//        ProgressWheel pw = new ProgressWheel(HomePageActivity.this, myAttributes);
        pw = (ProgressWheel) findViewById(R.id.pw_spinner);
        pw.startSpinning();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        Log.i("onCreate", "onCreate: "+dm.heightPixels);
//        Log.i("onCreate", "onCreate: "+dm.widthPixels);
        Intent serviceIntent = new Intent(HomePageActivity.this, HttpService.class);
        startService(serviceIntent); //启动后台服务
        //注册广播接收器
        receiver=new HomePageActivity.MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.whut.smartinspection.services.HttpService");
        HomePageActivity.this.registerReceiver(receiver,filter);

        initData();
    }
    /**
     * 获取广播数据
     *
     * @author jiqinlin
     *
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String count=bundle.getString("flag");
            if("1".equals(count)){
                pw.stopSpinning();
                pw.setVisibility(View.GONE);
                llList.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loopSlidingView.startTurning(4000);
    }
    private void initData(){
        showBanner();
    }
    @Override
    protected void onPause() {
        super.onPause();
        loopSlidingView.stopTurning();
    }
    @OnClick({R.id.ll_my_task,R.id.ll_help})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.ll_my_task:
                intent = new Intent(this, WaitingTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void showBanner() {
        List<Integer> urlList = new ArrayList<>();
//            urlList.add("http://img.taopic.com/uploads/allimg/120727/201995-120HG1030762.jpg");
        urlList.add(R.drawable.oop1);
        urlList.add(R.drawable.oop2);
        loopSlidingView.setImageOverride(urlList);
        loopSlidingView.startTurning(4000);
    }
}
