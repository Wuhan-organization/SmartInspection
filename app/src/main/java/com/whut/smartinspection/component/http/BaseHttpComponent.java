package com.whut.smartinspection.component.http;

public class BaseHttpComponent {
	public static final String URL = "http://115.159.108.163:8009/smartIsland/";
	public final String contentType = "application/json;charset=UTF-8";
	public static final String USER_AGENT = "fm_android";
	//获取变电站名称列表
	public static final String URL_SUBSTATION = "http://119.29.191.32:8080/BDZXJService/Substation/all";
	//任务提交
	public static final String URL_CO = "http://172.19.137.30:8080/BDZXJService_war/Substation/commitTask";
	//设备类型
	public static final String URL_DEVICE_STYLE = "http://119.29.191.32:8080/BDZXJService/Android/DeviceType/all";
	//间隔
	public static final String URL_INTERVALUNIT = "http://119.29.191.32:8080/BDZXJService/Android/IntervalUnit?sub_id=297e867e5f9a5670015f9a5e0f900002";
	//设备名称
	public static final String URL_DEVICE = "http://119.29.191.32:8080/BDZXJService/Android/Device?iu_id=297e867e5fa502a5015fa5059ddb0004";
	//巡视作业卡
	public static final String URL_PATROLNAME = "http://119.29.191.32:8080/BDZXJService/Android/PatrolContent?dev_id=2c9309a45fc8db67015fc8dec56600f1";

//	protected static FinalHttp mFinalHttp;
//	public static final String SET_COOKIE = "Set-Cookie";
//	public static final String SESSION_ID = "sessionid";
//	public static CookieStore cookieStore;
//
//	static {
//		mFinalHttp = FinalHttp.create();
//		mFinalHttp.configUserAgent(USER_AGENT);
//		mFinalHttp.configTimeout(5000);
//	}
	
}
