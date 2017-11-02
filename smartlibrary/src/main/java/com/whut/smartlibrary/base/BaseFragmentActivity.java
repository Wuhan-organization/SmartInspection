package com.whut.smartlibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BaseFragmentActivity extends FragmentActivity {

    // 在一个FragmentActivity中最多显示的Fragment的数量
    protected static final int MAX_FRAGMENT_COUNT = 10;

    private List<BaseFragment> mFragmentList = new ArrayList<BaseFragment>();

    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mFragmentManager = getSupportFragmentManager();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getCurrrent() != null) {
            getCurrrent().onNewIntent(intent);
        }
    }

    @Override
    public void onBackPressed() {

        if (getCurrrent() != null && getCurrrent().onBackPressed()) {
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getCurrrent() != null) {
            getCurrrent().onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 获取资源文件中的尺寸的值
     * 
     * @param resId
     *            资源id
     * @return 资源文件中的尺寸的值
     */
    protected int getDimen(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 显示特定Fragment
     * 
     * @param containerId
     *            需要加入到的container的Id
     * @param fragment
     *            需要展示的Fragment
     * @param bundle
     *            Fragment的额外数据, 会将该bundle设置到Fragment的argument中
     */
    public void showFragment(int containerId, BaseFragment fragment,
            Bundle bundle) {
        if (fragment == null) {
            return;
        }

        fragment.setArguments(bundle);

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (getCurrrent() == null) {
            ft.add(containerId, fragment).commit();
        } else if (getCurrrent() == fragment) {
            ft.show(fragment).commit();
        } else {
            if (fragment.isAdded()) {
                ft.hide(getCurrrent()).show(fragment).commit();
                fragment.onLoadData(fragment.getArguments());
            } else {
                ft.hide(getCurrrent()).add(containerId, fragment).commit();
            }
        }

        // 先移除 再添加, 为了让最新的在最上面
        if (mFragmentList.contains(fragment)) {
            mFragmentList.remove(fragment);
        }
        mFragmentList.add(fragment);

        // 当栈中的Fragment太多的时候，就移除掉最底部的fragment
        if (mFragmentList.size() > MAX_FRAGMENT_COUNT) {
            for (int i = 0; i < mFragmentList.size(); i++) {
                BaseFragment f = mFragmentList.get(i);
                if (f.canPopFromStack()) {
                    mFragmentList.remove(i);
                    FragmentTransaction ft2 = mFragmentManager
                            .beginTransaction();
                    ft2.remove(f).commit();
                    break;
                }
            }
        }
    }

    /**
     * 显示特定Fragment
     * 
     * @param containerId
     *            需要加入到的container的Id
     * @param fragment
     *            需要展示的Fragment
     */
    public void showFragment(int containerId, BaseFragment fragment) {
        showFragment(containerId, fragment, null);
    }

    /**
     * 将Fragment从当前Activity中移除
     */
    public void popFragment() {
        if (mFragmentList.isEmpty()) {
            finish();
            return;
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        BaseFragment current = mFragmentList.get(mFragmentList.size() - 1);
        ft.remove(current);

        mFragmentList.remove(mFragmentList.size() - 1);

        // 所有的Fragment都被清除了, 直接Finish当前Activity
        if (mFragmentList.isEmpty()) {
            finish();
            return;
        }

        BaseFragment show = mFragmentList.get(mFragmentList.size() - 1);
        ft.show(show).commit();
        show.onLoadData(show.getArguments());
    }

    /**
     * 获取当前展示的Fragment
     * 
     * @return 当前展示的Fragment
     */
    public BaseFragment getCurrrent() {
        if (mFragmentList.isEmpty()) {
            return null;
        }

        return mFragmentList.get(mFragmentList.size() - 1);
    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics(); 
        displayMetrics.scaledDensity = displayMetrics.density;
    }
}
