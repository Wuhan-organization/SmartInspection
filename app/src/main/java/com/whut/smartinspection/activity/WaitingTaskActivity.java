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

import org.greenrobot.greendao.query.Query;
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
                        intent = new Intent(WaitingTaskActivity.this,FullInspectActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        item.setStatus(1);
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
        TaskItemDao taskItemDao = SApplication.getInstance().getDaoSession().getTaskItemDao();
        Query<TaskItem> qb = taskItemDao.queryBuilder().build();
        List<TaskItem> listTemp = qb.listLazyUncached();

        if(listTemp.size()>0){
            for (int i= 0;i<SApplication.getTaskCount();i++){
                TaskItem item = listTemp.get(i);
                if("0".equals(item.getTaskType())){
                    item.setTaskTypeName("全面巡视");
                    item.setTaskIcon(R.drawable.bian_dian);
                }
                list.add(item);
            }
            taskPageListAdapter.notifyDataSetChanged();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TaskItemDao taskItemDao = SApplication.getInstance().getDaoSession().getTaskItemDao();
                    Query<TaskItem> qb = taskItemDao.queryBuilder().build();
                    List<TaskItem> listTemp = qb.listLazyUncached();
                    if(listTemp.size()>0)
                    for (int i= 0;i<SApplication.getTaskCount();i++){
                        TaskItem item = listTemp.get(i);
                        if("0".equals(item.getTaskType())){
                            item.setTaskTypeName("全面巡视");
                            item.setTaskIcon(R.drawable.bian_dian);
                        }
                        list.add(item);
                    }
                }
            }).start();
            taskPageListAdapter.notifyDataSetChanged();
        }

    }
}
