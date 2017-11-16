package com.whut.smartinspection.component.http;

import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.model.ResultObject;
import com.whut.smartinspection.parser.CustomParser;
import com.whut.smartinspection.parser.WeatherParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Fortuner on 2017/11/15.
 */

public class TaskComponent extends BaseHttpComponent {

    public static final String url = "http://119.29.191.32:8080/BDZXJService_war/Substation/all";
    //获取变电站名称列表
    public static void getSubstationList(final IHandlerListener listener) {
        OkHttpUtils.post().
                url(url).
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
                ResultObject ro = WeatherParser.parseWeather(response);
                if (ro.getResults() != null) {
                    listener.onSuccess(ro.getResults(), EMsgType.GET_WEATHER_SUCCESS);
                } else {
                    listener.onFailure(ro.getStatus(), EMsgType.GET_WEATHER_FAILURE);
                }

            }
        });
    }
}
