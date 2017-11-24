package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.greendao.gen.SubDao;
import com.whut.smartinspection.adapters.PageViewTabAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Sub;
import com.whut.smartinspection.utils.TaskUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import com.whut.smartinspection.R;


//import static com.baidu.location.h.j.R;

/**
 * 全面巡视页面
 * Created by xiongbin on 2017/11/2.
 */
public class FullInspectionActivity extends SwipeBackActivity implements ITaskHandlerListener{

    @BindView(R.id.tv_full_inspection_back)
    TextView tvFullInspectionBack; // 返回
    @BindView(R.id.style_device)
    EditText styleDevice;  //设备类型
    @BindView(R.id.substation_name)
    EditText substationName;
    @BindView(R.id.name_device)
    EditText nameDevice;//设备名称
    @BindView(R.id.name_dis)
    EditText nameDis ;//间隔名称
    @BindView(R.id.work_walk)
    EditText workWalk;//巡视作业
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.vp_view)
    ViewPager mViewPager;
    @BindView(R.id.scorll_view)
    ScrollView scrollView;
    final ArrayList<String> suList = new ArrayList<>();
    final ArrayList<String> stList = new ArrayList<>();
    final ArrayList<String> woList = new ArrayList<>();
    final ArrayList<String> ndList = new ArrayList<>();
    final ArrayList<String> neList = new ArrayList<>();

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspection);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        //滑动样式
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        TaskComponent.getSubstationList(FullInspectionActivity.this,suList);
        TaskUtils.initView(substationName,suList,"变电站名称",FullInspectionActivity.this);

        TaskComponent.getDeviceStyleList(FullInspectionActivity.this,stList);
        TaskUtils.initView(styleDevice,stList,"设备类型",FullInspectionActivity.this);

        woList.add("全面巡视");
        woList.add("专业巡视");
        TaskUtils.initView(workWalk,woList,"巡视作业",FullInspectionActivity.this);

        TaskComponent.getSubstationList(FullInspectionActivity.this,ndList);
        TaskUtils.initView(nameDis,ndList,"间隔名称",FullInspectionActivity.this);

        TaskComponent.getDeviceStyleList(FullInspectionActivity.this,stList);
        TaskUtils.initView(nameDevice,stList,"设备名称",FullInspectionActivity.this);

        mInflater = getLayoutInflater();
        view1 = mInflater.inflate(R.layout.item_tabs, null);
        view2 = mInflater.inflate(R.layout.item_tabs, null);
        view3 = mInflater.inflate(R.layout.item_tabs, null);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);

        //添加页卡标题
        mTitleList.add("缺陷");
        mTitleList.add("隐患");
        mTitleList.add("问题");


        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(1);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        PageViewTabAdapter mAdapter = new PageViewTabAdapter(mViewList,mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

    }
    @OnClick({R.id.tv_full_inspection_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_full_inspection_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object obj, EMsgType type,ArrayList<String> list) {
        JsonObject jsonObject = new JsonParser().parse((String)obj).getAsJsonObject();

        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        for(int i = 0;i<jsonArray.size();i++) {
            JsonElement idx = jsonArray.get(i);
            JsonObject jo = idx.getAsJsonObject();

            String id = jo.get("id").toString();

            String name = jo.get("name").toString();

            if(null != name&&name.length()>2)
            list.add(name.substring(1,name.length()-1));
//            Sub sub = new Sub(id,name);
//
//            SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
//            subDao.insert(sub);
//
//            List<Sub> li = subDao.queryBuilder().where(SubDao.Properties.Id.eq(id)).build().list();
//            Sub s = li.get(0);


        }
    }

    @Override
    public void onFailure(Object obj, EMsgType type) {

    }
}
