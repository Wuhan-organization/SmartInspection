package com.whut.smartinspection.component.http;

public class BaseHttpComponent {
	public static final String URL = "http://115.159.108.163:8009/smartIsland/";
	public final String contentType = "application/json;charset=UTF-8";
	public static final String USER_AGENT = "fm_android";
//	public static final String IP = "http://119.27.171.24/";
	public static final String IP = "http://192.168.1.104:8080/";
	//获取变电站名称列表
	public static final String URL_SUBSTATION =IP + "BDZXJService/Android/Substation/all";
	//任务提交
	public static final String URL_CO = IP + "BDZXJService_war/Substation/commitTask";
	//设备类型
	public static final String URL_DEVICE_STYLE =IP +  "BDZXJService/Android/DeviceType/all";
	//间隔
		public static final String URL_INTERVALUNIT =IP +  "BDZXJService/Android/IntervalUnit/all";
	//设备名称
	public static final String URL_DEVICE =IP +  "BDZXJService/Android/Device";
	//巡视项目
	public static final String URL_PATROLNAME =IP +  "BDZXJService/Android/PatrolContent";
	//任务查询
	public static final String URL_TASK =IP + "BDZXJService/Android/Task";
	//巡视作业卡
	public static final String URL_PATROL_NAME =IP + "BDZXJService/Android/PatrolName";
	public static final String URL_HeadPage =IP + "BDZXJService/Android/HeadPage";
	public static final String URL_LOGIN =IP + "BDZXJService/Android/Login";
	public static final String URL_PatrolRecord =IP + "BDZXJService/Android/PatrolRecord";
	//通用任务列表
	public static final String URL_COMMON_TASK_LIST=IP + "BDZXJService/Android/Task";
	//获得详细任务内容
	public static final String URL_PatrolTask=IP + "BDZXJService/Android/PatrolTask";


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
