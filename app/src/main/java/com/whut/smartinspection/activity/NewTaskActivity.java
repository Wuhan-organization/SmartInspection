package com.whut.smartinspection.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.smartinspection.R;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartlibrary.base.SwipeBackActivity;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTaskActivity extends SwipeBackActivity implements ITaskHandlerListener {


    Map<String,String> map = new HashMap<String,String>();

    @BindView(R.id.number_task)
    TextView numberTask;
    @BindView(R.id.discribe_content)
    TextView discribeContent;
    @BindView(R.id.button_substation)
    TextView substationText;

    final ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);

        TaskComponent.getSubstationList(NewTaskActivity.this,0);

        initView();
    }

    private void initView(){

        substationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus == true && list.size()!= 0){
                    final WheelViewDialog dialog = new WheelViewDialog(NewTaskActivity.this);
                    dialog.setTitle("变电站名称").setItems(list).setButtonText("确定").setCount(5).setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                        @Override
                        public void onItemClick(int position, String s) {
                           substationText.setText(s);
                        }
                    }).show();
                }
            }
        });

    }
    @OnClick({R.id.tv_new_back,R.id.button_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_new_back:
                finish();
                break;
            case R.id.button_commit:
                getText();
                TaskComponent.commitTask(NewTaskActivity.this,map);
                break;
            default:
                break;
        }
    }
    @Override
    public void onTaskSuccess(Object obj, EMsgType type, final int flag) {
        JsonObject jsonObject = new JsonParser().parse((String)obj).getAsJsonObject();

        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        for(int i = 0;i<jsonArray.size();i++){
            JsonElement idx = jsonArray.get(i);
            JsonObject jo = idx.getAsJsonObject();

            String id = jo.get("id").toString();
            String name = jo.get("name").toString();

            list.add(name.substring(1,name.length()-1));
        }
    }
    @Override
    public void onTaskFailure(Object obj, EMsgType type) {

    }
    private void getText(){
        String numT = numberTask.getText().toString();
        String sub = substationText.getText().toString();
        String dis = discribeContent.getText().toString();
        if(checkText(numberTask,numT)&&checkText(substationText,sub)&&checkText(discribeContent,dis)){
            map.put("substation",sub);
            map.put("discribeContent",dis);
            map.put("number",numT);
        }
    }
    private boolean checkText(TextView t,String s){
        if(s == null|| "".equals(s)){
            t.setFocusable(true);
            return false;
        }
        return true;
    }
}
