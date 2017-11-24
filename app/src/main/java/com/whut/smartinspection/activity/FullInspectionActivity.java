package com.whut.smartinspection.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.SubDao;
import com.whut.smartinspection.adapters.PageViewTabAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
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

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;


//import static com.baidu.location.h.j.R;

/**
 * 全面巡视页面
 * Created by xiongbin on 2017/11/2.
 */
public class FullInspectionActivity extends SwipeBackActivity{

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
    @BindView(R.id.title_content)
    TextView titleContent;//标题题目
    @BindView(R.id.back)
    FloatingActionButton back;//上一个
    @BindView(R.id.next)
    FloatingActionButton next;//下一个
    final ArrayList<String> suList = new ArrayList<>();
    final ArrayList<String> stList = new ArrayList<>();
    final ArrayList<String> woList = new ArrayList<>();
    final ArrayList<String> ndList = new ArrayList<>();
    final ArrayList<String> neList = new ArrayList<>();

    final List<PatrolContent> lPatrolContent = new ArrayList<>();

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspection);
        ButterKnife.bind(this);
//        back = (FloatingActionButton) findViewById(R.id.back);
//        next = (FloatingActionButton) findViewById(R.id.next);

        initData();

        initView();
    }

    private void initData(){
        //从greenDao查询变电站名称
        SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
        QueryBuilder<Sub> qb = subDao.queryBuilder();

        List<Sub> list = qb.list();
        for (Sub sub   :list ) {
            suList.add(sub.getName());
        }
        //从greenDao查询设备名称
        DeviceTypeDao deviceTypeDao = SApplication.getInstance().getDaoSession().getDeviceTypeDao();
        QueryBuilder<DeviceType> qbDT = deviceTypeDao.queryBuilder();
        List<DeviceType> listDT = qbDT.list();
        for (DeviceType dt   :listDT ) {
            stList.add(dt.getName());
        }
        //间隔名称
        IntervalUnitDao intervalUnitDao = SApplication.getInstance().getDaoSession().getIntervalUnitDao();
        QueryBuilder<IntervalUnit> qbIU = intervalUnitDao.queryBuilder();

        List<IntervalUnit> listIU = qbIU.list();
        for (IntervalUnit iu   :listIU ) {
            ndList.add(iu.getName());
        }
        //巡视作业
        woList.add("全面巡视");
        woList.add("例行巡视");
        woList.add("特殊巡视");
        woList.add("特殊巡视");
        woList.add("熄灯巡视");
        //设备名称
        DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
        QueryBuilder<Device> qbD = deviceDao.queryBuilder();
        List<Device> listD = qbD.list();
        for (Device dt   :listD ) {
            neList.add(dt.getName());
        }
        //巡视作业卡片内容
        PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
        QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
        List<PatrolContent> listPC = qbPC.list();
        for (PatrolContent dt  :listPC ) {
            lPatrolContent.add(dt);
        }

    }
    private void initView(){
        //滑动样式
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        TaskUtils.initView(substationName,suList,"变电站名称",FullInspectionActivity.this);

        TaskUtils.initView(styleDevice,stList,"设备类型",FullInspectionActivity.this);

        TaskUtils.initView(workWalk,woList,"巡视作业",FullInspectionActivity.this);

        TaskUtils.initView(nameDis,ndList,"间隔名称",FullInspectionActivity.this);

        TaskUtils.initView(nameDevice,neList,"设备名称",FullInspectionActivity.this);

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

        titleContent.setText(lPatrolContent.get(0).getNo()+"."+lPatrolContent.get(0).getContent());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = titleContent.getText().toString();
                int point = Integer.parseInt(content.split("\\.")[0]);
                point--;
                if(point>=0)
                    titleContent.setText(lPatrolContent.get(point%lPatrolContent.size()).getNo()+
                            "."+lPatrolContent.get(point%lPatrolContent.size()).getContent());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = titleContent.getText().toString();
                int point = Integer.parseInt(content.split("\\.")[0]);
                point++;
                titleContent.setText(lPatrolContent.get(point%lPatrolContent.size()).getNo()+
                        "."+lPatrolContent.get(point%lPatrolContent.size()).getContent());
            }
        });
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
}
