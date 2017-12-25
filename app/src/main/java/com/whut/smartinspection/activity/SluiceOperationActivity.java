package com.whut.smartinspection.activity;

import android.os.Bundle;

import com.whut.smartinspection.R;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2017/12/21.
 * 道闸操作
 */

public class SluiceOperationActivity extends SwipeBackActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sluice_operation);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    private void initView(){
        CustomToolBar.goBack(SluiceOperationActivity.this);//返回按钮监听
    }
    private void initData(){

    }
}
