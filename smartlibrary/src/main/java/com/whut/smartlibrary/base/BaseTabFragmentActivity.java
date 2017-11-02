package com.whut.smartlibrary.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class BaseTabFragmentActivity extends BaseFragmentActivity {

    // Fragment堆栈
    private SparseArray<List<BaseFragment>> mFragmentStatcks = new SparseArray<List<BaseFragment>>();
    // 当前Fragment的栈
    private int mCurrentStack = 0;

    @Override
    public void showFragment(int containerId, BaseFragment fragment) {
        showFragment(containerId, fragment, null);
    }

    @Override
    public void showFragment(int containerId, BaseFragment fragment,
            Bundle bundle) {
        if (fragment == null) {
            return;
        }

        int stackTag = fragment.getStackTag();
        if (stackTag < 0) {
            stackTag = 0;
        }
        showFragment(containerId, stackTag, fragment, null);
    }

    /**
     * 显示特定Fragment
     * 
     * @param containerId
     *            需要加入到的container的Id
     * @param stackTag
     *            需要展示在哪个堆栈中
     * @param fragment
     *            需要展示的Fragment
     */
    public void showFragment(int containerId, int stackTag,
            BaseFragment fragment) {
        showFragment(containerId, stackTag, fragment, null);
    }

    /**
     * 显示特定Fragment
     * 
     * @param containerId
     *            需要加入到的container的Id
     * @param stackTag
     *            需要展示在哪个堆栈中
     * @param fragment
     *            需要展示的Fragment
     * @param bundle
     *            Fragment的额外数据, 会将该bundle设置到Fragment的argument中
     */
    public void showFragment(int containerId, int stackTag,
            BaseFragment fragment, Bundle bundle) {
        if (fragment == null) {
            return;
        }

        fragment.setArguments(bundle);
        mCurrentStack = stackTag;

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

        List<BaseFragment> fragmentList = mFragmentStatcks.get(mCurrentStack);
        if (fragmentList == null) {
            fragmentList = new ArrayList<BaseFragment>();
            mFragmentStatcks.put(mCurrentStack, fragmentList);
        }

        // 先移除 再添加, 为了让最新的在最上面
        if (fragmentList.contains(fragment)) {
            fragmentList.remove(fragment);
        }
        fragmentList.add(fragment);

        // 当栈中的Fragment太多的时候，就移除掉最底部的fragment
        if (fragmentList.size() > MAX_FRAGMENT_COUNT) {
            for (int i = 0; i < fragmentList.size(); i++) {
                BaseFragment f = fragmentList.get(i);
                if (f.canPopFromStack()) {
                    fragmentList.remove(i);
                    FragmentTransaction ft2 = mFragmentManager
                            .beginTransaction();
                    ft2.remove(f).commit();
                    break;
                }
            }
        }
    }

    @Override
    public void popFragment() {
        List<BaseFragment> fragmentList = mFragmentStatcks.get(mCurrentStack);
        if (fragmentList == null || fragmentList.isEmpty()) {
            finish();
            return;
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        BaseFragment current = fragmentList.get(fragmentList.size() - 1);
        ft.remove(current);

        fragmentList.remove(fragmentList.size() - 1);

        // 所有的Fragment都被清除了, 直接Finish当前Activity
        if (fragmentList.isEmpty()) {
            finish();
            return;
        }

        BaseFragment show = fragmentList.get(fragmentList.size() - 1);
        ft.show(show).commit();
        show.onLoadData(show.getArguments());
    }

    @Override
    public BaseFragment getCurrrent() {
        List<BaseFragment> fragmentList = mFragmentStatcks.get(mCurrentStack);
        if (fragmentList == null || fragmentList.isEmpty()) {
            return null;
        }

        return fragmentList.get(fragmentList.size() - 1);
    }

}
