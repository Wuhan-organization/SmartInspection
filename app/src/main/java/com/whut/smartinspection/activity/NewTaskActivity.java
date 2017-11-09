package com.whut.smartinspection.activity;

import android.app.Activity;
import android.os.Bundle;

import com.whut.smartinspection.R;
import com.whut.smartlibrary.base.SwipeBackActivity;

public class NewTaskActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
    }
}
