package com.whut.smartinspection.utils;

import android.view.View;
import android.widget.EditText;

import com.whut.smartinspection.activity.FullInspectionActivity;
import com.whut.smartinspection.activity.NewTaskActivity;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Task;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;

/**
 * Created by Fortuner on 2017/11/21.
 */

public class TaskUtils {

    public static void initView(final EditText editText, final ArrayList<String> list, final String title,
                                final FullInspectionActivity fullInspectionActivity, final Task task){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()!= 0){
                    final WheelViewDialog dialog = new WheelViewDialog(fullInspectionActivity);
                    dialog.setTitle(title).setItems(list).setButtonText("确定").setCount(5).
                            setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                                @Override
                                public void onItemClick(int position, String s) {
                                    editText.setText(s);
                                }
                            }).show();
                    switch (title){
                        case "变电站名称":
                            task.setSubName(editText.getText().toString());
                            break;
                        case "设备类型":
                            task.setGetDeviceName(editText.getText().toString());
                            break;
                        case "巡视作业":
                            task.setPatrolContentName(editText.getText().toString());
                        break;
                        case "间隔名称":
                            task.setIntervalUnitName(editText.getText().toString());
                            break;
                        case "设备名称":
                            task.setDeviceName(editText.getText().toString());
                            break;
                        default:
                            break;
                    }
                }
            }

        });



    }
}
