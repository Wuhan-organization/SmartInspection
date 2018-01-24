package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.whut.smartinspection.R;
import com.whut.smartinspection.activity.tree_view.TreeTestActivity;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tw.com.a_i_t.IPCamViewer.HelmetActivity;

/**
 * Created by Fortuner on 2017/12/15.
 * 帮助界面
 */

public class HelpActivity  extends SwipeBackActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        CustomToolBar.goBack(HelpActivity.this);
    }
    @OnClick({R.id.first,R.id.second,R.id.third,R.id.fouth})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.first:
                intent = new Intent(HelpActivity.this, HelmetActivity.class);
                startActivity(intent);
                break;
            case R.id.second:
                break;
            case R.id.third:
                intent = new Intent(HelpActivity.this, BluetoothActivity.class);
                startActivity(intent);
                break;
            case R.id.fouth:
//                intent = new Intent(HelpActivity.this, TreeTestActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
