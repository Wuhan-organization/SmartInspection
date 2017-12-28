package com.whut.smartinspection.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whut.greendao.gen.TaskItemDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.db.BaseDbComponent;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.http.UserComponent;
import com.whut.smartinspection.utils.SystemUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/***
 * 登录页面
 */
public class LoginActivity extends SwipeBackActivity implements IHandlerListener {

    @BindView(R.id.btn_login_user_login)
    Button btnUserLogin;

    @BindView(R.id.et_login_username)
    EditText etLoginUsername;

    @BindView(R.id.et_login_password)
    EditText etLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //删除任务列表
        TaskItemDao taskItemDao = BaseDbComponent.getTaskItemDao();
        taskItemDao.deleteAll();

        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        String username = sp.getString("USERNAME","");
        String password = sp.getString("PASSWORD","");
        etLoginUsername.setText("li");
        etLoginPassword.setText("123456");

        if(SApplication.getSessionID()!=null && !"".equals(SApplication.getSessionID())){
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({R.id.btn_login_user_login, R.id.btn_login_reset, R.id.btn_login_user_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            // 登录
            case R.id.btn_login_user_login:
                login();
                break;
            case R.id.btn_login_reset:
                etLoginPassword.setText("");
                etLoginUsername.setText("");
                break;
            case R.id.btn_login_user_exit:
                finish();
                break;
            default:
                break;
        }
    }

    private void login() {
        String username = etLoginUsername.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();
        UserComponent.login(this, username, password);
    }

    @Override
    public void onSuccess(Object obj, EMsgType type) {
        switch (type) {
            // 登录成功
            case LOGIN_SUCCESS:
                String sessionID = (String)obj;//将sesionID存入全局变量
                SApplication.setSessionID(sessionID);
                //将用户名密码保存
                SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
                sp.edit().putString("USERNAME",etLoginUsername.getText().toString());
                sp.edit().putString("PASSWORD",etLoginPassword.getText().toString());
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Object obj, EMsgType type) {
        switch (type) {
            // 登录失败
            case LOGIN_FAILURE:
                SystemUtils.showToast(this, obj.toString());
                break;
            default:
                break;
        }
    }
}
