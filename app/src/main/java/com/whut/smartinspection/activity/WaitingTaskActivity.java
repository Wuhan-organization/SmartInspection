package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whut.greendao.gen.PatrolTaskDetailDao;
import com.whut.greendao.gen.SubDao;
import com.whut.greendao.gen.TaskItemDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.TaskPageListAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.db.BaseDbComponent;
import com.whut.smartinspection.model.PatrolTaskDetail;
import com.whut.smartinspection.model.Sub;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaitingTaskActivity extends SwipeBackActivity {
    @BindView(R.id.gd_task_page_menu)
    ListView taskmenu;

    TaskPageListAdapter taskPageListAdapter ;
    List<TaskItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_task);
        ButterKnife.bind(this);
        CustomToolBar.goBack(WaitingTaskActivity.this);//返回按钮监听
        initView();
        initData();
    }

    private void initView(){

        taskPageListAdapter = new TaskPageListAdapter(this,list);
        taskmenu.setAdapter(taskPageListAdapter);

        taskmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                TaskPageListAdapter tpl = (TaskPageListAdapter)adapterView.getAdapter();
                TaskItem item  = (TaskItem) tpl.getItem(i);
                switch (Integer.parseInt(item.getTaskType())){
                    case 0://全面巡视
                        intent = new Intent(WaitingTaskActivity.this,FullInspectionActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    case 6://倒闸操作
                        intent = new Intent(WaitingTaskActivity.this,SluiceOperationActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    case 7://带电检测
                        intent = new Intent(WaitingTaskActivity.this,PointDetectionActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    case 8://运维
                        intent = new Intent(WaitingTaskActivity.this,MaintenanceActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });


    }
    private void initData(){
//        TaskItemDao taskItemDao = SApplication.getInstance().getDaoSession().getTaskItemDao();
        TaskItemDao taskItemDao = BaseDbComponent.getTaskItemDao();
        QueryBuilder<TaskItem> qb = taskItemDao.queryBuilder();
        List<TaskItem> listTemp = qb.build().list();
//        PatrolTaskDetailDao patrolTaskDetail = SApplication.getInstance().getDaoSession().getPatrolTaskDetailDao();
//        QueryBuilder<PatrolTaskDetail> qbPatrolTDetail = patrolTaskDetail.queryBuilder();

//        while(listTemp.size()==0){
//            try{
//                Thread.sleep(500);
//            }catch (Exception e){
//
//            }
//            listTemp = qb.build().list();
//        }
        for (int i= 0;i<listTemp.size();i++){
            TaskItem item = listTemp.get(i);
            if("0".equals(item.getTaskType())){
                item.setTaskTypeName("全面巡视");
                item.setTaskIcon(R.drawable.bian_dian);
            }
            list.add(item);
        }
//        TaskItem item = new TaskItem();
//        item.setTaskType("0");
//        item.setWorker("范国柱");
//        item.setTaskTypeName("全面巡视");
//        item.setTaskIcon(R.drawable.bian_dian);
//        list.add(item);

//        TaskItem item1 = new TaskItem();
//        item1.setTaskType("6");
//        item1.setWorker("范国柱");
//        item1.setTaskTypeName("倒闸操作");
//        item1.setTaskIcon(R.drawable.dao_zha);
//        list.add(item1);
//
//        TaskItem item2 = new TaskItem();
//        item2.setTaskType("7");
//        item2.setTaskTypeName("带电检测");
//        item2.setTaskIcon(R.drawable.patrol);
//        item2.setWorker("范国柱");
//        list.add(item2);
//
//        TaskItem item3 = new TaskItem();
//        item3.setTaskType("8");
//        item3.setTaskTypeName("运维");
//        item3.setWorker("范国柱");
//        item3.setTaskIcon(R.drawable.yun_wei);
//        list.add(item3);
        taskPageListAdapter.notifyDataSetChanged();
    }
}
