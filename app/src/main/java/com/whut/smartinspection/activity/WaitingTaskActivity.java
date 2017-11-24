package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.TaskPageListAdapter;
import com.whut.smartinspection.model.Task;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaitingTaskActivity extends SwipeBackActivity {


    @BindView(R.id.gd_task_page_menu)
    ListView taskmenu;

    TaskPageListAdapter taskPageListAdapter ;
    List<TaskPageListAdapter.TaskPageItem> list = new ArrayList<TaskPageListAdapter.TaskPageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_task);

        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView(){

        taskPageListAdapter = new TaskPageListAdapter(this,list);
        taskmenu.setAdapter(taskPageListAdapter);
        final Intent intent = new Intent(this,FullInspectionActivity.class);
        taskmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskPageListAdapter tpl = (TaskPageListAdapter)adapterView.getAdapter();
                TaskPageListAdapter.TaskPageItem item  = (TaskPageListAdapter.TaskPageItem)tpl.getItem(i);

                startActivity(intent);

            }
        });


    }
    @OnClick({R.id.tv_wai_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_wai_back:
                finish();
                break;
            default:
                break;
        }
    }
    private void initData(){


        for(int i = 0;i<20;i++){
            TaskPageListAdapter.TaskPageItem item = new TaskPageListAdapter.TaskPageItem();
            item.setText("texttesttexttesttexttesttexttesttexttesttexttesttexttest" +
                    "testtexttesttexttesttexttesttexttesttexttesttexttesttextt"+i);
            item.setNumber("number"+i);
            item.setStationName("stationName"+i);
            list.add(item);
        }
        taskPageListAdapter.notifyDataSetChanged();
    }
}
