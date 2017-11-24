package com.whut.smartinspection.component.http;

import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.parser.CustomParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Fortuner on 2017/11/15.
 */

public class TaskComponent extends BaseHttpComponent {

   public static void getSubstationList(final ITaskHandlerListener listener, final int flag) {
        OkHttpUtils.get().
                url(URL_SUBSTATION).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onTaskFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                listener.onTaskSuccess(response, EMsgType.GET_WEATHER_SUCCESS,1);

            }
        });

       //设备类型
       OkHttpUtils.get().
               url(URL_DEVICE_STYLE).
               build().execute(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {
               String message = "network error";
               if (e != null) {
                   message = e.getMessage();
               }
               listener.onTaskFailure(message, EMsgType.LOGIN_FAILURE);
           }

           @Override
           public void onResponse(String response, int id) {
               listener.onTaskSuccess(response, EMsgType.GET_WEATHER_SUCCESS,2);

           }
       });
       //获取间隔
       OkHttpUtils.get().
               url(URL_INTERVALUNIT).
               build().execute(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {
               String message = "network error";
               if (e != null) {
                   message = e.getMessage();
               }
               listener.onTaskFailure(message, EMsgType.LOGIN_FAILURE);
           }

           @Override
           public void onResponse(String response, int id) {
               listener.onTaskSuccess(response, EMsgType.GET_WEATHER_SUCCESS,3);

           }
       });
       //获取设备名称
       OkHttpUtils.get().
               url(URL_DEVICE).
               build().execute(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {
               String message = "network error";
               if (e != null) {
                   message = e.getMessage();
               }
               listener.onTaskFailure(message, EMsgType.LOGIN_FAILURE);
           }

           @Override
           public void onResponse(String response, int id) {
               listener.onTaskSuccess(response, EMsgType.GET_WEATHER_SUCCESS,5);

           }
       });
       //获取巡视作业卡
       OkHttpUtils.get().
               url(URL_PATROLNAME).
               build().execute(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {
               String message = "network error";
               if (e != null) {
                   message = e.getMessage();
               }
               listener.onTaskFailure(message, EMsgType.LOGIN_FAILURE);
           }

           @Override
           public void onResponse(String response, int id) {
               listener.onTaskSuccess(response, EMsgType.GET_WEATHER_SUCCESS,6);

           }
       });
    }

    public static void commitTask(final ITaskHandlerListener listener,Map<String,String> map) {


        OkHttpUtils.post().
                url(URL_CO).params(map).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onTaskFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                CustomParser.ResponseObject ro = CustomParser.parse(response);
                if (ro.getCode() == 200) {
                    listener.onTaskSuccess(ro.getMsg(), EMsgType.LOGIN_SUCCESS,0);
                } else {
                    listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }

            }
        });
    }
}
