package com.whut.smartinspection.component.http;

import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.parser.CustomParser;
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
     * @param username
     * @param password
     */
    public static void login(final IHandlerListener listener, String username, String password) {

        String url = "http://172.19.137.30:8080/BDZXJService/android/login";
        OkHttpUtils.post().
                url(url).
                addParams("username", username).
                addParams("password", password).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                listener.onFailure(e.getMessage(), EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                CustomParser.ResponseObject ro = CustomParser.parse(response);
                if (ro.getRet() == 200) {
                    listener.onSuccess(ro.getMsg(), EMsgType.LOGIN_SUCCESS);
                } else {
                    listener.onFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }

            }
        });
    }


}
