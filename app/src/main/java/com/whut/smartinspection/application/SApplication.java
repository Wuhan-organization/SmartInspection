package com.whut.smartinspection.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;

import com.whut.greendao.gen.DaoMaster;
import com.whut.greendao.gen.DaoSession;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/*****
 * @author xiongbin
 * @since 2015-06-01
 */
public class SApplication extends Application {
	
	private static SApplication instance;
	private static String sessionID = "";

	private static boolean isInitTaskDetail = false;
	private DaoMaster.DevOpenHelper mHelper;
	private SQLiteDatabase db;
	private DaoMaster mDaoMaster;
	private DaoSession mDaoSession;

	public static boolean isInitTaskDetail() {
		return isInitTaskDetail;
	}

	public static void setIsInitTaskDetail(boolean isInitTaskDetail) {
		SApplication.isInitTaskDetail = isInitTaskDetail;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		setSystemLanguage();
		
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常  
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

		initOkHttp();
		setDatabase();
	}

	private void initOkHttp() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				//其他配置
				.build();

		OkHttpUtils.initClient(okHttpClient);
	}

	/**
	 * 设置greenDao
	 */
	private void setDatabase() {
		// 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
		// 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
		// 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
		// 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
		mHelper = new DaoMaster.DevOpenHelper(this, "smartInspection_db", null);
		db = mHelper.getWritableDatabase();
		// 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
		mDaoMaster = new DaoMaster(db);
		mDaoSession = mDaoMaster.newSession();
	}

	public DaoSession getDaoSession() {
		return mDaoSession;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	/***
	 * 获得应用程序Application
	 * @return 应用程序Application
	 */
	public static SApplication getInstance(){
		return instance;
	}
	
	/*****
	 * 设置系统语言
	 */
	private void setSystemLanguage(){
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();//获得设置对象  
		DisplayMetrics dm = resources .getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。  
		config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文  
		resources.updateConfiguration(config, dm);
	}

	public static String getSessionID() {
		return sessionID;
	}

	public static void setSessionID(String sessionID) {
		SApplication.sessionID = sessionID;
	}
}
