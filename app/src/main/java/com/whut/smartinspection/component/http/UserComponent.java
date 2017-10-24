package com.whut.smartinspection.component.http;

import com.whut.smartinspection.component.handler.IHandlerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @author linhong
 * @version 1.0
 * @time 2016年10月9日 上午10:43:42
 * @description 用户相关接口
 */
public class UserComponent extends BaseHttpComponent {

    /***
     * 登录
     * @param listener
     * @param userName
     * @param password
     */
    public static void login(IHandlerListener listener, String userName, String password) {

        OkHttpUtils.get().
                url("").
                addParams("userName", userName).
                addParams("password", password).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }


}
