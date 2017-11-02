package com.whut.smartlibrary.base;

import android.app.Application;

public class BaseApplication extends Application {

    private boolean mIsDebugMode = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public boolean isDebugMode() {
        return mIsDebugMode;
    }
}
