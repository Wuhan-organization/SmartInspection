package com.whut.smartinspection.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*** 
 * @author xiongbin
 * @date 2016-5-29 下午3:48:36
 * @version 1.0
 * @description 服务端返回数据的通用解析器
 * 服务端返回数据格式如下
{
    "msg": "ok",
    "data": "",
    "code": 0
}
 */
public class CustomParser {
	
	public final static int SUCCESS = 0;
	
	/***
	 * 服务端返回的数据json解析
	 * @param json
	 * @return
	 * 		ResponseObject对象
	 */
	public static ResponseObject parse(String json){
		ResponseObject mo = new ResponseObject();
		try {
			JSONObject obj = new JSONObject(json);
			mo.setData(obj.optString("data"));
			mo.setMsg(obj.optString("msg"));
			mo.setCode(obj.optInt("code"));
			return mo;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/***
	 * 泛型方法，返回ResponseObject中字段data对应的java实体
	 * 调用方式：CustomParser.<Object>parseData(ResponseObject);
	 * 其中Object为目标java实体
	 * @param ro
	 * 			ResponseObject实体
	 * @return
	 * 			返回ResponseObject中字段data对应的java实体
	 */
	public static <T> T parseData(ResponseObject ro, Class<T> cls){
		String data = ro.getData();
		Gson gson = new Gson();
		T t = gson.fromJson(data, cls);
		return t;
	}
	
	/**
	 * 
	 * @param data 对象json字符串
	 * @return
	 */
	public static<T> T parseData(String data, Class<T> cls){
		Gson gson = new Gson();
		T t = gson.fromJson(data, cls);
		return t;
	}
	
	/***
	 * 泛型方法，返回ResponseObject中字段data_list对应的java实体List
	 * 调用方式：CustomParser.<List<Object>>parseDataList(ResponseObject);
	 * 其中List<Object>为目标java实体List
	 * @param ro
	 * 			ResponseObject实体
	 * @return
	 * 			返回ResponseObject中字段data对应的java实体List
	 */
	public static <T> List<T> parseDataList(ResponseObject ro, Class<T> cls) {
		String data = ro.getData();
		try {
			List<T> alist = new ArrayList<T>();
			Gson gson = new Gson();
			JsonArray array = new JsonParser().parse(data).getAsJsonArray();
			for (JsonElement elem : array) {
				alist.add(gson.fromJson(elem, cls));
			}
			return alist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * 泛型方法，返回ResponseObject中字段data_list对应的java实体List
	 * 调用方式：CustomParser.<List<Object>>parseDataList(ResponseObject);
	 * 其中List<Object>为目标java实体List
	 * @param ro
	 * 			ResponseObject实体
	 * @return
	 * 			返回ResponseObject中字段data对应的java实体List
	 */
	public static <T> List<T> parseDataList2(ResponseObject ro, Class<T> cls) {
		String data = ro.getData();
		try {
			JSONObject json = new JSONObject(data);
			String dataList = json.getString("data_list");
			List<T> alist = new ArrayList<T>();
			Gson gson = new Gson();
			JsonArray array = new JsonParser().parse(dataList).getAsJsonArray();
			for (JsonElement elem : array) {
				alist.add(gson.fromJson(elem, cls));
			}
			return alist;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static class ResponseObject{
		private String msg;
		private String data;
		private int code;
		
		/***
		 * getter 和  setter方法区
		 */
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
	}
}
