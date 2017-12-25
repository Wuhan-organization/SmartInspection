package com.whut.smartinspection.activity;

import android.os.Bundle;

import com.whut.smartinspection.R;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2017/12/24.
 */

public class MaintenanceActivity extends SwipeBackActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        ButterKnife.bind(this);
        CustomToolBar.goBack(MaintenanceActivity.this);//返回按钮监听
    }
}
