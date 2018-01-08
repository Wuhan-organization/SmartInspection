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

import com.whut.greendao.gen.PersonDao;
import com.whut.greendao.gen.TaskItemDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.db.BaseDbComponent;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.http.UserComponent;
import com.whut.smartinspection.model.Person;
import com.whut.smartinspection.utils.ButtonUtils;
import com.whut.smartinspection.utils.SystemUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/***
 * 登录页面
 */
public class LoginActivity extends SwipeBackActivity implements IHandlerListener {
    private String username ;
    private String password;
    private  String sessionID;
    @BindView(R.id.btn_login_user_login)
    Button btnUserLogin;

    @BindView(R.id.et_login_username)
    EditText etLoginUsername;

    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    PersonDao personDao = SApplication.getInstance().getDaoSession().getPersonDao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //删除任务列表
        BaseDbComponent.deleteData();

        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        username = sp.getString("USERNAME","");
        password = sp.getString("PASSWORD","");
        sessionID = sp.getString("SESSIONID","");
//        if((username==null||"".equals(username))&&(password==null||"".equals(password))){
//            QueryBuilder<Person> qbPerson = personDao.queryBuilder();
//            List<Person> personList = qbPerson.where(PersonDao.Properties.Id.eq(1L)).list();
//            username = personList.size()>0?personList.get(0).getName():"";
//            password = personList.size()>0?personList.get(0).getPassword():"";
//            sessionID = personList.size()>0?personList.get(0).getSessionId():"";
//            SApplication.setSessionID(sessionID);
//        }
        etLoginPassword.setText("123456");
        etLoginUsername.setText("yang");
        if(SApplication.getSessionID()!=null && !"".equals(SApplication.getSessionID())){
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
        etLoginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    username = etLoginUsername.getText().toString();
                    QueryBuilder<Person> qbPerson = personDao.queryBuilder();
                    List<Person> personList = qbPerson.where(PersonDao.Properties.Name.eq(username)).list();
                    password = personList.size()>0?personList.get(0).getPassword():"";
                    etLoginPassword.setText(password);
                }
            }
        });
    }

    @OnClick({R.id.btn_login_user_login, R.id.btn_login_reset, R.id.btn_login_user_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            // 登录
            case R.id.btn_login_user_login:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_login_user_login)) {
                    //写你相关操作即可
                    login();
                }
//                login();
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
                sessionID = (String)obj;//将sesionID存入全局变量
                SApplication.setSessionID(sessionID);
                //将用户名密码保存
                SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
                Person person = new Person();
                person.setId(1L);
                person.setName(username);
                person.setPassword(password);
                person.setSessionId(sessionID);
                personDao.insertOrReplace(person);
                //将用户信息存入全局变量
                sp.edit().putString("USERNAME",etLoginUsername.getText().toString());
                sp.edit().putString("PASSWORD",etLoginPassword.getText().toString());
                sp.edit().putString("SESSIONID",sessionID);
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
