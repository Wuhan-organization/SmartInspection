package com.whut.smartinspection.utils;

import android.view.View;
import android.widget.EditText;

import com.whut.smartinspection.activity.FullInspectionActivity;
import com.whut.smartinspection.activity.NewTaskActivity;
import com.whut.smartinspection.component.http.TaskComponent;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;

/**
 * Created by Fortuner on 2017/11/21.
 */

public class TaskUtils {

    public static void initView(final EditText editText, final ArrayList<String> list, final String title,
                                final FullInspectionActivity fullInspectionActivity){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true && list.size()!= 0){
                    final WheelViewDialog dialog = new WheelViewDialog(fullInspectionActivity);
                    dialog.setTitle(title).setItems(list).setButtonText("确定").setCount(5).
                            setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                        @Override
                        public void onItemClick(int position, String s) {
                            editText.setText(s);
                        }
                    }).show();
                }
            }
        });



    }
}
