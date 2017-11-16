package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.ResultObject;
import com.whut.smartinspection.model.Task;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskActivity extends SwipeBackActivity implements IHandlerListener {


    @BindView(R.id.title_task)
    TextView titleTask;
    @BindView(R.id.name_transformer_substation)
    Spinner nameTS;
    @BindView(R.id.number_task)
    TextView numberTask;
    @BindView(R.id.discribe_content)
    TextView discribeContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);

        TaskComponent.getSubstationList(NewTaskActivity.this);

        titleTask.setText("新建任务");
        List<String> list = new ArrayList<>();

        list.add("上海");
        list.add("北京");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameTS.setAdapter(arrayAdapter);

        nameTS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter arrayAdapter1 = (ArrayAdapter)parent.getAdapter();
                String str = (String)arrayAdapter1.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onSuccess(Object obj, EMsgType type) {
        ResultObject.Result[] results = (ResultObject.Result[]) obj;
        for(int i = 0;i<results.length;i++){
            Log.i("i",results[i].toString());
        }
    }

    @Override
    public void onFailure(Object obj, EMsgType type) {

    }
}
