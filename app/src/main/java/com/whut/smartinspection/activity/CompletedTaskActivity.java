package com.whut.smartinspection.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whut.smartinspection.R;
import com.whut.smartlibrary.base.SwipeBackActivity;

public class CompletedTaskActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
    }
}
