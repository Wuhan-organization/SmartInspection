package com.whut.smartinspection.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

        Intent serviceIntent = new Intent(HomePageActivity.this, HttpService.class);
        startService(serviceIntent); //启动后台服务
        initData();
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
