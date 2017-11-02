package com.whut.smartlibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whut.smartlibrary.R;


/**
 * 所有Fragment都要继承该类<br/>
 * 
 * @author Kingt
 * 
 */
public class BaseFragment extends Fragment {

    private View contentView;
    private int stackTag = -1;
    private boolean isInited = false;
    // 是否允许从Activity中移除
    private boolean canPopFromStack = true;

    protected LoadDataHandler loadDataHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView != null) {
            return contentView;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInited = true;

        onLoadData(getArguments());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 设置展示的View
     * 
     * @param contentView
     */
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    /**
     * 设置展示的View
     * 
     * @param layoutId
     */
    public void setContentView(int layoutId) {
        this.contentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }

    /**
     * 通过id查找特定的View
     * 
     * @param id
     * @return
     */
    public View findViewById(int id) {
        if (contentView == null) {
            return null;
        }

        return contentView.findViewById(id);
    }

    /**
     * 通过id查找特定的View
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        if (contentView == null) {
            return null;
        }

        return (T) contentView.findViewById(id);
    }

    /**
     * 当物理返回键按下的时候, 会调用该函数
     * 
     * @return 如果该函数返回值为true, 则不会执行对应FragmentActivity的onBackPressed()方法,
     *         额外的返回按键处理, 可以在这个函数里做; 默认为false
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 当Activity的onNewIntent被调用的时候, 会调用当前Fragment的onNewIntent方法,
     * 同时将intent中的extra信息保存到Fragment的argument中
     * 
     * @param intent
     */
    public void onNewIntent(Intent intent) {

    }

    public BaseFragmentActivity getFragmengActivity() {
        return (BaseFragmentActivity) getActivity();
    }

    /**
     * 在界面加载完成后，会自动调用该函数。控件赋值的操作，可以在该函数中处理
     * 
     * @param bundle
     *            默认为Fragment的 getArgument()
     */
    public void onLoadData(Bundle bundle) {

    }

    /**
     * 是否允许从FragmentActivity中将该Fragment移除. 通常底部的几个Tab对应的Fragment不允许移除, 其他的都可以
     * 
     * @return 是否允许从FragmentActivity中将该Fragment移除
     */
    public boolean canPopFromStack() {
        return canPopFromStack;
    }

    /**
     * 是否允许从FragmentActivity中将该Fragment移除. 通常底部的几个Tab对应的Fragment不允许移除, 其他的都可以
     */
    public void setCanPopFromStack(boolean canpop) {
        canPopFromStack = canpop;
    }

    /**
     * 获取该Fragment对应的栈的Tag. -1 表示不在任何有标识的栈中
     * 
     * @return 获取该Fragment对应的栈的Tag.
     */
    public int getStackTag() {
        return stackTag;
    }

    /**
     * 设置该Fragment对应的栈的Tag. 0 表示不在任何有标识的栈中
     */
    public void setStackTag(int stackTag) {
        this.stackTag = stackTag;
    }

    /**
     * 获取资源文件中的颜色的值
     * 
     * @param resId
     * @return 资源文件中的颜色的值
     */
    protected int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取资源文件中的尺寸的值
     * 
     * @param resId
     * @return 资源文件中的尺寸的值
     */
    protected int getDimen(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 界面是否初始化完成了
     * 
     * @return 界面是否初始化完成了
     */
    public boolean isInited() {
        return isInited;
    }

    public static interface LoadDataHandler {
        public void onLoadSuccess(Object... result);
    }

    public LoadDataHandler getLoadDataHandler() {
        return loadDataHandler;
    }

    public void setLoadDataHandler(LoadDataHandler loadDataHandler) {
        this.loadDataHandler = loadDataHandler;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.in_from_right, 0);
    }
}
