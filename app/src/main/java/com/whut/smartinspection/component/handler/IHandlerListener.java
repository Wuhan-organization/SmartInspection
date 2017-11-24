package com.whut.smartinspection.component.handler;

import java.util.ArrayList;

/***
 * @author xiongbin
 * @date 2016-3-22
 * @version 1.0
 * @description 请求数据成功与否的回调接口
 */
public interface IHandlerListener {
	public void onSuccess(Object obj, EMsgType type);
	public void onFailure(Object obj, EMsgType type);
}
