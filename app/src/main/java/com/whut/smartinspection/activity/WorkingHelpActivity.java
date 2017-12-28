package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whut.smartinspection.R;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fortuner on 2017/12/28.
 * 巡检项目时帮助
 */

public class WorkingHelpActivity extends SwipeBackActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_help);
        ButterKnife.bind(this);
        CustomToolBar.goBack(WorkingHelpActivity.this);//返回按钮监听
        initView();
        initData();
    }
    private void initView(){

    }
    private void initData(){

    }
    @OnClick({R.id.toolbar_right_tv})
    public void onClick(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.toolbar_right_tv:
                intent = new Intent(WorkingHelpActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
