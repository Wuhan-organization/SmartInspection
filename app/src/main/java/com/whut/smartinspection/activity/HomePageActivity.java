package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.whut.smartinspection.R;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fortuner on 2017/12/14.
 */

public class HomePageActivity extends SwipeBackActivity {

    @BindView(R.id.ll_help)
    LinearLayout llHelp;
    @BindView(R.id.ll_my_task)
    LinearLayout llMyTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

    }
    @OnClick({R.id.ll_my_task,R.id.ll_help})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_my_task:
                Intent intent = new Intent(this, WaitingTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_help:
                break;
            default:
                break;
        }
    }
}
