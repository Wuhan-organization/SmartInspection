package com.whut.baidu.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/*** 
 * @author xiongbin
 * @date 2016-6-23 上午10:20:26
 * @version 1.0
 * @description 百度地图定位服务
 */
public class LocationService {
	private static LocationService instance;
	
	private LocationClient mLocationClient;
	private BDLocationListener listener;
	
	public static LocationService getInstance(Context context) {
		if (instance == null) {
			instance = new LocationService(context);
		}
		return instance;
	}
	
	private LocationService(Context context){
        //实例化定位服务，LocationClient类必须在主线程中声明
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationClient.setLocOption(initLocationOption());
	}
	
	/***
	 * 注册监听器
	 * @param listener
	 */
	public LocationService registerListener(BDLocationListener listener){
		if (instance == null) {
			return null;
		}
		this.listener = listener;
		mLocationClient.registerLocationListener(listener);//注册定位监听接口
		return instance;
	}
	
	/****
	 * 配置定位SDK参数
	 * @return
	 * 		LocationClientOption 定位服务参数
	 */
	private LocationClientOption initLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		return option;
	}
	
	/*** 开始定位  ***/
	public void startLocation(){
		mLocationClient.start();  // 调用此方法开始定位
	}
	
	/*** 停止定位  ***/
	public void stopLocation(){
		mLocationClient.unRegisterLocationListener(listener);
		mLocationClient.stop();  // 调用此方法停止定位
	}
}
