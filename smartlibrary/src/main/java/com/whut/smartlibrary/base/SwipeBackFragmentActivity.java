package com.whut.smartlibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whut.smartlibrary.R;
import com.whut.smartlibrary.lib.SwipeBackActivityBase;
import com.whut.smartlibrary.lib.SwipeBackActivityHelper;
import com.whut.smartlibrary.lib.SwipeBackLayout;
import com.whut.smartlibrary.lib.Utils;


public class SwipeBackFragmentActivity extends BaseFragmentActivity implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    protected int activityInAnim;
    protected int activityOutAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    @Override
    public void finish(){
        super.finish();
        if (activityOutAnim == 0) {
            overridePendingTransition(0, R.anim.out_to_right);
        } else {
            overridePendingTransition(0, activityOutAnim);
        }
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        // R.anim.anim_stay为结束页面的动画，可以解决黑屏问题
        overridePendingTransition(R.anim.in_from_right, R.anim.anim_stay);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        // R.anim.anim_stay为结束页面的动画，可以解决黑屏问题
        overridePendingTransition(R.anim.in_from_right, R.anim.anim_stay);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // R.anim.anim_stay为结束页面的动画，可以解决黑屏问题
        overridePendingTransition(R.anim.in_from_right, R.anim.anim_stay);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        // R.anim.anim_stay为结束页面的动画，可以解决黑屏问题
        overridePendingTransition(R.anim.in_from_right, R.anim.anim_stay);
    }
}
