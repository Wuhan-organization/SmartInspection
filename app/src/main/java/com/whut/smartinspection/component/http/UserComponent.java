package com.whut.smartinspection.component.http;

import android.util.Log;

import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.parser.CustomParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

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
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpUtils.postString().url(URL_LOGIN).mediaType(JSON).content("{\"username\":\""+username+"\",\"password\":\""+password+"\"}").
                build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                if (response.code() == 200) {
                    String re = response.headers().get("Set-Cookie");
                    String sessionID = re.split(";")[0]+";";
                    listener.onSuccess(sessionID, EMsgType.LOGIN_SUCCESS);
                } else {
                    listener.onFailure("返回码不是200", EMsgType.LOGIN_FAILURE);
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(Object response, int id) {
            }
        });
    }


}
