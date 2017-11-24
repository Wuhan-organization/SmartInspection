package com.whut.smartinspection.component.handler;

import java.util.ArrayList;

/**
 * Created by Fortuner on 2017/11/21.
 */

public interface ITaskHandlerListener {
    public void onSuccess(Object obj, EMsgType type,ArrayList<String> list);
    public void onFailure(Object obj, EMsgType type);
}
