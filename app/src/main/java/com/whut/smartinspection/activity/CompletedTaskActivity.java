package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.TaskPageListAdapter;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.whut.smartinspection.adapters.TaskPageListAdapter.TaskPageItem;

public class CompletedTaskActivity extends SwipeBackActivity {


    @BindView(R.id.gd_task_page_menu)
    ListView taskmenu;

    TaskPageListAdapter taskPageListAdapter ;
    List<TaskPageListAdapter.TaskPageItem> list = new ArrayList<TaskPageListAdapter.TaskPageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
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
                TaskPageItem item  = (TaskPageItem)tpl.getItem(i);

                startActivity(intent);

            }
        });


    }
    private void initData(){


        for(int i = 0;i<20;i++){
            TaskPageItem item = new TaskPageItem();
            item.setText("texttesttexttesttexttesttexttesttexttesttexttesttexttest" +
                    "texttesttexttesttexttesttexttesttexttesttexttesttexttestte"+i);
            item.setNumber("number"+i);
            item.setStationName("stationName"+i);
            list.add(item);
        }
        taskPageListAdapter.notifyDataSetChanged();
    }
    @OnClick({R.id.tv_com_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_com_back:
                finish();
                break;
            default:
                break;
        }
    }

}
