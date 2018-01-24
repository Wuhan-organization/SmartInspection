package com.whut.smartinspection.component.handler;

import java.util.ArrayList;

/**
 * Created by Fortuner on 2017/11/21.
 */

public interface ITaskHandlerListener {
    public void onTaskSuccess(Object obj, EMsgType type, int flag);
    public void onTaskFailure(Object obj, EMsgType type,int flag);
}
