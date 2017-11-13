package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.model.Task;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskActivity extends SwipeBackActivity {


    @BindView(R.id.title_task)
    TextView titleTask;
    @BindView(R.id.name_transformer_substation)
    TextView nameTS;
    @BindView(R.id.number_task)
    TextView numberTask;
    @BindView(R.id.discribe_content)
    TextView discribeContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        String  flag = intent.getStringExtra("flag");
        if(flag.equals("1")){
            title = "已完成任务详情";
            nameTS.setEnabled(false);
            nameTS.setFocusable(false);
            numberTask.setEnabled(false);
            numberTask.setFocusable(false);
            discribeContent.setEnabled(false);
            discribeContent.setFocusable(false);
        }else {
            title = "待办任务详情";
        }
        if(flag.equals("2")){
            title = "新建任务";
        }
        titleTask.setText(title);
        Task task = (Task)intent.getSerializableExtra("task");

        nameTS.setText(task.getStationName());
        numberTask.setText(task.getNumber());
        discribeContent.setText(task.getText());

    }
}
