package com.whut.smartinspection.activity;

import android.os.Bundle;

import com.whut.smartinspection.R;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2017/12/21.
 * 带电检测
 */

public class PointDetectionActivity extends SwipeBackActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detection);
        ButterKnife.bind(this);

        initView();
        initData();
    }
    private void initView(){
        CustomToolBar.goBack(PointDetectionActivity.this);//返回按钮监听
    }
    private void initData(){

    }
}
