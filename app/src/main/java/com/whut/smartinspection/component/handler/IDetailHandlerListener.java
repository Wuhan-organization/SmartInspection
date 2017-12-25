package com.whut.smartinspection.component.handler;

/**
 * Created by Fortuner on 2017/12/24.
 */

public interface IDetailHandlerListener {
    public void onDetialSuccess(Object obj, EMsgType type, int flag,String taskId);
    public void onDetialFailure(Object obj, EMsgType type);
}
