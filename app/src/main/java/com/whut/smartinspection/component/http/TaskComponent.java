package com.whut.smartinspection.component.http;

import com.google.gson.JsonObject;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.model.ResultObject;
import com.whut.smartinspection.parser.CustomParser;
import com.whut.smartinspection.parser.WeatherParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static android.R.id.list;

/**
 * Created by Fortuner on 2017/11/15.
 */

public class TaskComponent extends BaseHttpComponent {

    public static final String URL_SUBSTATION = "http://119.29.191.32:8080/BDZXJService/Substation/all";
    //获取变电站名称列表
    public static final String URL_DEVICE_STYLE = "http://119.29.191.32:8080/BDZXJService/Android/DeviceType?sub_id=";
    public static void getSubstationList(final ITaskHandlerListener listener, final ArrayList<String> list) {
        OkHttpUtils.get().
                url(URL_SUBSTATION).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                listener.onSuccess(response, EMsgType.GET_WEATHER_SUCCESS,list);

            }
        });
    }

    public static void getDeviceStyleList(final ITaskHandlerListener listener,final ArrayList<String> list) {
        OkHttpUtils.get().
                url(URL_DEVICE_STYLE).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                listener.onSuccess(response, EMsgType.GET_WEATHER_SUCCESS,list);

            }
        });
    }
    public static void commitTask(final ITaskHandlerListener listener,Map<String,String> map) {

        String url = "http://172.19.137.30:8080/BDZXJService_war/Substation/commitTask";
        OkHttpUtils.post().
                url(url).params(map).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                CustomParser.ResponseObject ro = CustomParser.parse(response);
                if (ro.getCode() == 200) {
                    listener.onSuccess(ro.getMsg(), EMsgType.LOGIN_SUCCESS,null);
                } else {
                    listener.onFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }

            }
        });
    }
}
