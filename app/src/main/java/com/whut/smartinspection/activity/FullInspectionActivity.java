package com.whut.smartinspection.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
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
import com.whut.smartinspection.model.Task;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.utils.TaskUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import com.whut.smartinspection.R;

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
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.button_id_visual)
    LinearLayout linearLayout;
    EditText content1;
    EditText content2;
    EditText content3;
    final ArrayList<String> suList = new ArrayList<>();
    final ArrayList<String> stList = new ArrayList<>();
    final ArrayList<String> woList = new ArrayList<>();
    final ArrayList<String> ndList = new ArrayList<>();
    final ArrayList<String> neList = new ArrayList<>();

    Map<Integer,String> map = new HashMap<Integer,String>();
    Map<String,String> radioMap = new HashMap<String,String>();
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    private Task task = new Task();

    TaskItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspection);
        ButterKnife.bind(this);

        item = (TaskItem)getIntent().getSerializableExtra("item");
        String pageFlag = getIntent().getStringExtra("pageFlag");
        if("1".equals(pageFlag)){//已完成界面设置
            pageSetting();
        }

        initData();

        initView();
    }
    private void pageSetting(){
        linearLayout.setVisibility(View.INVISIBLE);

    }
    private void initData(){
        //从greenDao查询变电站名称
//        SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
//        QueryBuilder<Sub> qb = subDao.queryBuilder();
//
//        List<Sub> list = qb.list();
//        for (Sub sub   :list ) {
//            suList.add(sub.getName());
//        }
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
//        woList.add("全面巡视");
//        woList.add("例行巡视");
//        woList.add("特殊巡视");
//        woList.add("特殊巡视");
//        woList.add("熄灯巡视");
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
            map.put(dt.getNo(),dt.getContent());
        }
    }
    private void initView(){
        //滑动样式
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

//        TaskUtils.initView(substationName,suList,"变电站名称",FullInspectionActivity.this,task);
        substationName.setText(item.getContent());

        TaskUtils.initView(styleDevice,stList,"设备类型",FullInspectionActivity.this,task);

//        TaskUtils.initView(workWalk,woList,"巡视作业",FullInspectionActivity.this,task);
        substationName.setText("全面巡视");

        TaskUtils.initView(nameDis,ndList,"间隔名称",FullInspectionActivity.this,task);

        TaskUtils.initView(nameDevice,neList,"设备名称",FullInspectionActivity.this,task);

        mInflater = getLayoutInflater();
        view1 = mInflater.inflate(R.layout.item_tab1, null);
        view2 = mInflater.inflate(R.layout.item_tab2, null);
        view3 = mInflater.inflate(R.layout.item_tab3, null);


        content1 = (EditText) view1.findViewById(R.id.content1);
        content2 = (EditText) view2.findViewById(R.id.content2);
        content3 = (EditText) view3.findViewById(R.id.content3);

        content1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String s = content1.getText().toString();
                    task.setBug(s);
                }
            }
        });
        content2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String s = content2.getText().toString();
                    task.setDanger(s);
                }
            }
        });
        content3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String s = content3.getText().toString();
                    task.setProblem(s);
                }
            }
        });

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

        titleContent.setText(1+"."+map.get(1));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = titleContent.getText().toString();
                int point = Integer.parseInt(content.split("\\.")[0]);
                point--;
                if(point>0)
                    titleContent.setText(point+"."+map.get(point).substring(1,map.get(point).length()-1));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = titleContent.getText().toString();
                int point = Integer.parseInt(content.split("\\.")[0]);
                point++;
                if(point<=map.size())
                    titleContent.setText(point+"."+map.get(point).substring(1,map.get(point).length()-1));
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String content = titleContent.getText().toString().split("\\.")[0];
                if(checkedId == R.id.radioButton1){//是
                    radioMap.put(content,String.valueOf(true));
                }
                if(checkedId == R.id.radioButton2){//否
                    radioMap.put(content,String.valueOf(false));
                }
            }
        });
    }


    @OnClick({R.id.tv_full_inspection_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_full_inspection_back:// 返回
                finish();
                break;
            case R.id.task_detail_commit:
                if(radioMap.size() <map.size()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppMsg.makeText(FullInspectionActivity.this,"还有数据未录入...",AppMsg.STYLE_CONFIRM);
                        }
                    });
                }

                break;
            default:
                break;
        }
    }
    public Map<String,String> getDataCommit(){
        Map<String,String> map = new HashMap<>();

        String subName = substationName.getText().toString();
        String subId = "";
        String deviceName = nameDevice.getText().toString();
        String deviceStyle = styleDevice.getText().toString();
        String disName = nameDis.getText().toString();
        String walkwork = workWalk.getText().toString();

        String ontent1 = content1.getText().toString();
        String ontent2 = content2.getText().toString();
        String ontent3 = content3.getText().toString();

        map.put("subName",subName);
        map.put("subId",subId);
        map.put("deviceName",deviceName);
        map.put("deviceStyle",deviceStyle);
        map.put("disName",disName);
        map.put("Walkwork",walkwork);
        map.put("ontent1",ontent1);
        map.put("ontent2",ontent2);
        map.put("ontent3",ontent3);
        return map;
//        JsonObject jsonObject = new JsonObject();

    }
}
