package com.whut.smartinspection.component.http;

import android.util.Log;

import com.google.gson.JsonObject;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IDetailHandlerListener;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.parser.CustomParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Fortuner on 2017/11/15.
 */

public class TaskComponent extends BaseHttpComponent {
    private  static String sessionID = "";
   public static void getSubstationList(final ITaskHandlerListener listener, final int flag) {
        sessionID = SApplication.getSessionID();
        OkHttpUtils.get().addHeader("Cookie", sessionID).
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
                CustomParser.ResponseObject ro = CustomParser.parse(response);
                if (ro.getCode() == 200) {
                    listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,1);
                } else {
                    listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }
            }
        });

       //设备类型
       OkHttpUtils.get().addHeader("Cookie", sessionID).
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
               CustomParser.ResponseObject ro = CustomParser.parse(response);
               if (ro.getCode() == 0) {
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,2);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }
           }
       });
       //获取间隔
       OkHttpUtils.get().addHeader("Cookie", sessionID).
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
               CustomParser.ResponseObject ro = CustomParser.parse(response);
               if (ro.getCode() == 0) {
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,3);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }
           }
       });
       //获取设备名称
       OkHttpUtils.get().addHeader("Cookie", sessionID).
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
               CustomParser.ResponseObject ro = CustomParser.parse(response);
               if (ro.getCode() == 0) {
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,5);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }
           }
       });
       //获取巡视作业卡
       OkHttpUtils.get().addHeader("Cookie", sessionID).
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
//               listener.onTaskSuccess(response, EMsgType.GET_WEATHER_SUCCESS,6);
               CustomParser.ResponseObject ro = CustomParser.parse(response);
               if (ro.getCode() == 200) {
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,6);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }
           }
       });
       //查询任务
       OkHttpUtils.get().addHeader("Cookie", sessionID).
               url(URL_TASK).
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
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,7);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }

           }
       });
       //查询所有巡视作业卡
       OkHttpUtils.get().addHeader("Cookie", sessionID).
               url(URL_PATROL_NAME).
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
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,8);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }

           }
       });
       //获取通用任务列表
       OkHttpUtils.get().addHeader("Cookie", sessionID).
               url(URL_COMMON_TASK_LIST).
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
                   listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,9);
               } else {
                   listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
               }
           }
       });
    }
    public static void getDetialPatrolTask(final IDetailHandlerListener listener, String value, final String taskId){
        //获取详细任务内容
        OkHttpUtils.get().addHeader("Cookie", sessionID).
                url(URL_PatrolTask+"?task_id="+value).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onDetialFailure(message, EMsgType.LOGIN_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                CustomParser.ResponseObject ro = CustomParser.parse(response);
                if (ro.getCode() == 200) {
                    listener.onDetialSuccess(response, EMsgType.LOGIN_SUCCESS,1,taskId);
                } else {
                    listener.onDetialFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }
            }
        });
    }
    //提交任务
    public static void commitTask(final ITaskHandlerListener listener,String value) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpUtils.postString().content(value).mediaType(JSON).addHeader("Cookie", sessionID).
                url(URL_TASK).
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
    //查询任务
    public static void getHeadPageId(final ITaskHandlerListener listener,String value) {

        OkHttpUtils.postString().addHeader("Cookie", sessionID).
                url(URL_HeadPage).content(value).
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
                    listener.onTaskSuccess(response, EMsgType.LOGIN_SUCCESS,8);
                } else {
                    listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }

            }
        });
    }

    public static void commitDetialTask(final ITaskHandlerListener listener,String value) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpUtils.postString().addHeader("Cookie", sessionID).
                url(URL_PatrolRecord).mediaType(JSON).content(value).
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
                    listener.onTaskSuccess(ro.getMsg(), EMsgType.LOGIN_SUCCESS,11);
                } else {
                    listener.onTaskFailure(ro.getMsg(), EMsgType.LOGIN_FAILURE);
                }
            }
        });
    }
}
