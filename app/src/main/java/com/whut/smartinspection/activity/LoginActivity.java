package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whut.smartinspection.R;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
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
    }

    @OnClick({R.id.btn_login_user_login, R.id.btn_login_reset, R.id.btn_login_user_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            // 登录
            case R.id.btn_login_user_login:
                login();
                break;

            default:
                break;
        }
    }

    private void login() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        String username = etLoginUsername.getText().toString().trim();
//        String password = etLoginPassword.getText().toString().trim();
//        UserComponent.login(this, username, password);
    }

    @Override
    public void onSuccess(Object obj, EMsgType type) {
        switch (type) {
            // 登录成功
            case LOGIN_SUCCESS:
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
