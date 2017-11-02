package com.whut.smartlibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }


    protected int getDimen(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics(); 
        displayMetrics.scaledDensity = displayMetrics.density;
    }
}
