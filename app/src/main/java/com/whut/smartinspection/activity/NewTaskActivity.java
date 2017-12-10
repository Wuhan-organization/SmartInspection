package com.whut.smartinspection.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.greendao.gen.SubDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Sub;
import com.whut.smartlibrary.base.SwipeBackActivity;
import com.wx.wheelview.widget.WheelViewDialog;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    TempTask tempTask = new TempTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData(){
        //从greenDao查询变电站名称
        SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
        QueryBuilder<Sub> qb = subDao.queryBuilder();

        List<Sub> temp = qb.list();
        for (Sub sub   :temp ) {
            list.add(sub.getName());
        }
    }
    private void initView(){

        substationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()!= 0){
                    final WheelViewDialog dialog = new WheelViewDialog(NewTaskActivity.this);
                    dialog.setTitle("变电站名称").setItems(list).setButtonText("确定").setCount(5).setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                        @Override
                        public void onItemClick(int position, String s) {
                            substationText.setText(s);
                        }
                    }).show();
                }else{
                    Toast.makeText(NewTaskActivity.this,"数据库没有数据",Toast.LENGTH_LONG).show();
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
                Log.i("tempTask",tempTask.toString());
                TaskComponent.commitTask(NewTaskActivity.this,tempTask.toString());
                break;
            default:
                break;
        }
    }
    @Override
    public void onTaskSuccess(Object obj, EMsgType type, final int flag) {
        Toast.makeText(NewTaskActivity.this,"提交成功!",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onTaskFailure(Object obj, EMsgType type) {
        String message = (String)obj;
        Toast.makeText(NewTaskActivity.this,message,Toast.LENGTH_LONG).show();
    }
    private void getText(){
        String numT = numberTask.getText().toString();
        String subText = substationText.getText().toString();
        String dis = discribeContent.getText().toString();
        if(checkText(numberTask,numT)&&checkText(substationText,subText)&&checkText(discribeContent,dis)){
            SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
            QueryBuilder<Sub> qb = subDao.queryBuilder();
            Sub sTemp = null;
            if(subText.length()>2)
                sTemp = qb.where(SubDao.Properties.Name.eq(subText.substring(1,subText.length()-1))).unique();
            if(sTemp != null){
                tempTask.setId("");
                tempTask.setSubstationId(sTemp.getIdd());//变电站id
                tempTask.setWorker(numT);//工作成员
                tempTask.setContent(dis);//工作内容
                tempTask.setStatus(1);//1表示待办
                tempTask.setPatrolTypeId(1);//巡视类型
            }
        }
    }
    private boolean checkText(TextView t,String s){
        if(s == null|| "".equals(s)){
            if(t!=substationText){
                t.setFocusable(true);
                t.setFocusableInTouchMode(true);
                t.requestFocus();
            }
            Toast.makeText(NewTaskActivity.this,"还有内容未输入",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private class TempTask{

        private String id;
        private String content;
        private String type;
        private String worker;      //工作者
        private String date;
        private int status;       //完成状态1完成，0代办
        private String substationId;
        private int patrolTypeId;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"id\":\"")
                    .append(id).append('\"');
            sb.append(",\"content\":\"")
                    .append(content).append('\"');
            sb.append(",\"type\":\"")
                    .append(type).append('\"');
            sb.append(",\"worker\":\"")
                    .append(worker).append('\"');
            sb.append(",\"date\":\"")
                    .append(date).append('\"');
            sb.append(",\"status\":")
                    .append(status);
            sb.append(",\"substationId\":\"")
                    .append(substationId).append('\"');
            sb.append(",\"patrolTypeId\":")
                    .append(patrolTypeId);
            sb.append('}');
            return sb.toString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWorker() {
            return worker;
        }

        public void setWorker(String worker) {
            this.worker = worker;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSubstationId() {
            return substationId;
        }

        public void setSubstationId(String substationId) {
            this.substationId = substationId;
        }

        public int getPatrolTypeId() {
            return patrolTypeId;
        }

        public void setPatrolTypeId(int patrolTypeId) {
            this.patrolTypeId = patrolTypeId;
        }
    }
}
