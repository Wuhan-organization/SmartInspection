package com.whut.smartinspection.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolWorkCardDao;
import com.whut.smartinspection.adapters.PageViewTabAdapter;
import com.whut.smartinspection.adapters.TaskPageListAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.HeadPage;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.PatrolRecordsPostVo;
import com.whut.smartinspection.model.PatrolWorkCard;
import com.whut.smartinspection.model.Record;
import com.whut.smartinspection.model.Task;
import com.whut.smartinspection.utils.SystemUtils;
import com.whut.smartinspection.utils.TaskUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import com.whut.smartinspection.R;
import com.wx.wheelview.widget.WheelViewDialog;

import org.greenrobot.greendao.query.QueryBuilder;


//import static com.baidu.location.h.j.R;

/**
 * 全面巡视页面
 * Created by xiongbin on 2017/11/2.
 */
public class FullInspectionActivity extends SwipeBackActivity implements ITaskHandlerListener,GestureDetector.OnGestureListener,View.OnTouchListener {

    @BindView(R.id.rl_gestrue)
    RelativeLayout rlGestrue;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_patrol_card_name)
    TextView tvPatrolCardName;
    @BindView(R.id.first_part)
    TextView firstPart;
    @BindView(R.id.second_part)
    TextView secondPart;
    @BindView(R.id.degree_number)
    EditText degreeNumber;
    @BindView(R.id.secod_degree_number)
    EditText secondNumber;
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
    @BindView(R.id.degree_full)
    LinearLayout degree;
    @BindView(R.id.second_degree_full)
    LinearLayout secondDegree;
    EditText content1;
    EditText content2;
    EditText content3;
    final ArrayList<String> suList = new ArrayList<>();
    final ArrayList<String> stList = new ArrayList<>();
    final ArrayList<String> woList = new ArrayList<>();
    final ArrayList<String> ndList = new ArrayList<>();
    final ArrayList<String> neList = new ArrayList<>();
    List<Record> listR = new ArrayList<>();
    private int lastPoint = 0;

    Map<Integer,String> map = new HashMap<Integer,String>();
    Map<Integer,String> radioMap = new HashMap<Integer,String>();
    Map<Integer,String> radioMap1 = new HashMap<Integer,String>();
    private GestureDetector gestureDetector;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<Integer> degreeFlag = new ArrayList<>();
    private List<Integer> signFlg = new ArrayList<>();
    private List<Integer> radioFlag = new ArrayList<>();
    private Map<Integer,PatrolContent> pcList = new HashMap<>();
    private Map<Integer,PatrolContent> signPatrol = new HashMap<>();

    private Task task = new Task();
    private String patrolHeadPageId ;//首页ID

    private String patrolContentId ;//巡视作业卡名字ID
    private String deviceId ;//用于 请求首页ID的
    private String subId;
    private int deviceTypeIdd;
    public FullInspectionActivity(){
        gestureDetector = new GestureDetector(this);
    }

    TaskPageListAdapter.TaskPageItem item;
    String pageFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspection);
        ButterKnife.bind(this);

        item = (TaskPageListAdapter.TaskPageItem)getIntent().getSerializableExtra("item");
        subId = item.getId();

        pageFlag = getIntent().getStringExtra("pageFlag");
        if("1".equals(pageFlag)){//已完成界面设置
            pageSetting();
        }

        initData();

        initView();
    }
    private void pageSetting(){
        linearLayout.setVisibility(View.GONE);
        styleDevice.setEnabled(false);  //设备类型
        substationName.setEnabled(false);
        nameDevice.setEnabled(false);//设备名称
        nameDis.setEnabled(false); ;//间隔名称
        workWalk.setEnabled(false);//巡视作业
    }
    private void initData(){
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

    }
    private void initView(){
        //滑动样式
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        //左右滑
        gestureDetector.setIsLongpressEnabled(true);
        rlGestrue.setOnTouchListener(this);
        rlGestrue.setLongClickable(true);

        substationName.setText(item.getStationName());

        //设备类型选择
        styleDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stList.size()!= 0){
                    final WheelViewDialog dialog = new WheelViewDialog(FullInspectionActivity.this);
                    dialog.setTitle("设备类型").setItems(stList).setButtonText("确定").setCount(5).
                            setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                                @Override
                                public void onItemClick(int position, String s) {
                                    styleDevice.setText(s);
                                    //获取设备类型id
                                    DeviceTypeDao deviceTypeDao = SApplication.getInstance().getDaoSession().getDeviceTypeDao();
                                    QueryBuilder<DeviceType> dbDT = deviceTypeDao.queryBuilder();

                                    List<DeviceType> deviceTypeList1 = dbDT.where(DeviceTypeDao.Properties.Name.eq(s)).list();
                                    deviceTypeIdd = Integer.parseInt(deviceTypeList1.get(0).getIdd());

                                    //设备名称
                                    DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
                                    QueryBuilder<Device> qbD = deviceDao.queryBuilder();
//                                    List<Device> listD = qbD.list();
                                    List<Device> listD1 = qbD.where(DeviceDao.Properties.DeviceTypeId.eq(deviceTypeIdd)).list();
                                    for (Device dt   :listD1 ) {
                                        neList.add(dt.getName());
                                    }

                                    //巡视作业卡片内容
                                    map.clear();
                                    PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
                                    QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();

                                    List<PatrolContent> listPC = qbPC.where(PatrolContentDao.Properties.DeviceTypeId.eq(deviceTypeIdd)).list();
                                    for (PatrolContent dt  :listPC ) {
                                        int no = dt.getNo();
                                        if(!pcList.containsKey(no)){
                                            pcList.put(no,dt);//记录所有巡视项目
                                        }
                                        if(map.containsKey(no)){
                                            signPatrol.put(no,dt);
                                            continue;
                                        }
                                        if("0".equals(dt.getPatrolContentTypeNo())) {//单选
                                            radioFlag.add(dt.getNo());
                                            map.put(dt.getNo(), dt.getContent());
                                        }else if("1".equals(dt.getPatrolContentTypeNo())){//温度
                                            degreeFlag.add(dt.getNo());
                                            map.put(dt.getNo(),dt.getContent());
                                        } else{//备注
                                            signFlg.add(dt.getNo());
                                            map.put(dt.getNo(),dt.getContent());
                                        }
                                    }
                                    if(map.size()>0){
                                        titleContent.setText(1+"."+map.get(1).substring(1,map.get(1).length()-1));
                                        radioGroup.setVisibility(View.VISIBLE);
                                        next.setVisibility(View.VISIBLE);
                                        back.setVisibility(View.VISIBLE);
                                    }

                                }
                            }).show();
                    task.setGetDeviceName(styleDevice.getText().toString());

                }else  SystemUtils.showToast(FullInspectionActivity.this,"本地数据库无数据,请重启软件");

            }

        });


        workWalk.setText("全面巡视");

        TaskUtils.initView(nameDis,ndList,"间隔名称",FullInspectionActivity.this,task);

        //通过设备名称查巡视作业卡
        nameDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(neList.size()!= 0){
                    final WheelViewDialog dialog = new WheelViewDialog(FullInspectionActivity.this);
                    dialog.setTitle("设备名称").setItems(neList).setButtonText("确定").setCount(5).
                            setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                                @Override
                                public void onItemClick(int position, String s) {
                                    nameDevice.setText(s);
                                    DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
                                    QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
                                    List<Device> deviceList = qbDevice.where(DeviceDao.Properties.Name.eq(s)).list();
                                    if(deviceList!=null && deviceList.size()>0){
                                        deviceId = deviceList.get(0).getIdd();
                                    }
                                    PatrolWorkCardDao patrolWorkCardDao = SApplication.getInstance().getDaoSession().getPatrolWorkCardDao();
                                    QueryBuilder<PatrolWorkCard> dbPWC = patrolWorkCardDao.queryBuilder();
                                    List<PatrolWorkCard> parolWC = dbPWC.list();//where(PatrolWorkCardDao.Properties.DeviceId.eq(deviceId))
                                    if (parolWC!=null && parolWC.size()>0){
                                        PatrolWorkCard pwc = parolWC.get(0);
                                        patrolContentId = pwc.getIdd();//巡视作业卡名字ID
                                        tvPatrolCardName.setText(pwc.getName());
                                    }

                                    //请求首页ID
                                    HeadPage headPage = new HeadPage();
                                    headPage.setEditorName("110");
                                    headPage.setPatrolNameId(patrolContentId);
                                    headPage.setSubstationId(subId);
                                    String sTemp = headPage.toString();

                                    TaskComponent.getHeadPageId(FullInspectionActivity.this,sTemp);


                                }
                            }).show();
                            task.setDeviceName(nameDevice.getText().toString());
                }else {
                    SystemUtils.showToast(FullInspectionActivity.this,"本地数据库无数据,请重启软件");
                }
            }

        });

        mInflater = getLayoutInflater();
        view1 = mInflater.inflate(R.layout.item_tab1, null);
        view2 = mInflater.inflate(R.layout.item_tab2, null);
        view3 = mInflater.inflate(R.layout.item_tab3, null);


        content1 = (EditText) view1.findViewById(R.id.content1);
        content2 = (EditText) view2.findViewById(R.id.content2);
        content3 = (EditText) view3.findViewById(R.id.content3);
        if("1".equals(pageFlag)) {
            content1.setEnabled(false);
            content2.setEnabled(false);
            content3.setEnabled(false);
        }

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



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.clearCheck();
                degreeNumber.setFocusable(false);
                secondNumber.setFocusable(false);
                String content = titleContent.getText().toString();
                int point = Integer.parseInt(content.split("\\.")[0]);
                point--;
                if(point>0)
                    titleContent.setText(point+"."+map.get(point).substring(1,map.get(point).length()-1));
                if(radioFlag.contains(point)){//单选框
                    degree.setVisibility(View.GONE);
                    secondDegree.setVisibility(View.GONE);

                    radioGroup.setVisibility(View.VISIBLE);
                    if(!radioMap.containsKey(point)){//没录入清空
                        radioGroup.clearCheck();
                    }else{//已录入 设置
                        if(point<=map.size()&&"true".equals(radioMap.get(point)))
                            radioGroup.check(R.id.radioButton1);
                        if(point<=map.size()&&"false".equals(radioMap.get(point)))
                            radioGroup.check(R.id.radioButton2);
                    }
                }
                if(degreeFlag.contains(point)){//填温度数字
                    degree.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.GONE);
                    degreeNumber.setFocusable(true);
                    degreeNumber.setFocusableInTouchMode(true);
                    firstPart.setText(pcList.get(point).getPatrolContentName());

                    if(signPatrol.containsKey(point)){
                        secondDegree.setVisibility(View.VISIBLE);
                        secondNumber.setFocusable(true);
                        secondNumber.setFocusableInTouchMode(true);
                        secondPart.setText(signPatrol.get(point).getPatrolContentName());
                    }
                    lastPoint = point;
                }
                if(signFlg.contains(point)){

                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.clearCheck();
                degreeNumber.setFocusable(false);
                secondNumber.setFocusable(false);
                String content = titleContent.getText().toString();
                int point = Integer.parseInt(content.split("\\.")[0]);
                point++;
                if(point<=map.size())
                    titleContent.setText(point+"."+map.get(point).substring(1,map.get(point).length()-1));
                if(radioFlag.contains(point)){//单选框
                    degree.setVisibility(View.GONE);
                    secondDegree.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);
                    if(!radioMap.containsKey(point)){//没录入清空

                    }else{//已录入 设置
                        if(point<=map.size()&&"true".equals(radioMap.get(point)))
                            radioGroup.check(R.id.radioButton1);
                        if(point<=map.size()&&"false".equals(radioMap.get(point)))
                            radioGroup.check(R.id.radioButton2);
                    }
                }
                if(degreeFlag.contains(point)){//填温度数字

                    radioGroup.setVisibility(View.GONE);
                    if(radioMap.containsKey(point)){
                        degreeNumber.setText(radioMap.get(point));
                    }else{
                        degreeNumber.setText(null);
                    }
                    if(radioMap1.containsKey(point)){
                        secondNumber.setText(radioMap1.get(point));
                    }else{
                        secondNumber.setText(null);
                    }
                    degree.setVisibility(View.VISIBLE);
                    degreeNumber.setFocusable(true);
                    degreeNumber.setFocusableInTouchMode(true);
                    firstPart.setText(pcList.get(point).getPatrolContentName());
                    if(signPatrol.containsKey(point)){
                        secondDegree.setVisibility(View.VISIBLE);
                        secondNumber.setFocusable(true);
                        secondNumber.setFocusableInTouchMode(true);
                        secondPart.setText(signPatrol.get(point).getPatrolContentName());
                    }
                    lastPoint = point;
                }
                if(signFlg.contains(point)){

                }
            }
        });

        degreeNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    radioMap.put(lastPoint,degreeNumber.getText().toString());
                }
            }
        });
        secondNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    radioMap1.put(lastPoint,degreeNumber.getText().toString());
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String content = titleContent.getText().toString().split("\\.")[0];
                if(checkedId == R.id.radioButton1){//是
                    radioMap.put(Integer.parseInt(content),String.valueOf(true));
                }
                if(checkedId == R.id.radioButton2){//否
                    radioMap.put(Integer.parseInt(content),String.valueOf(false));
                }
            }
        });

    }


    @OnClick({R.id.tv_full_inspection_back,R.id.task_detail_commit,R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_full_inspection_back:// 返回
                finish();
                break;
            case R.id.task_detail_commit:
                if(radioMap.size() < map.size()){
                    new AlertDialog.Builder(FullInspectionActivity.this).setTitle("提示...")
                            .setMessage("数据未录入完，确认提交吗？")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    commitData();
                                }
                            }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }else{
                    commitData();
                }

                break;
            case R.id.tv_commit:
                if(map.size()==0 || radioMap.size() < map.size()){
                    new AlertDialog.Builder(FullInspectionActivity.this).setTitle("提示...")
                            .setMessage("数据未录入完，确认提交吗？")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    commitData();
                                }
                            }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }else{
                    commitData();
                }

            break;
            default:
                break;
        }
    }
    private void commitData(){
        PatrolRecordsPostVo patrolRecordsPostVo = new PatrolRecordsPostVo();
        for (Map.Entry<Integer,String> entry: radioMap.entrySet()
                ) {
            Record record = new Record();
            int i = entry.getKey();

            if(radioMap.get(i).equals(String.valueOf(false))){
                record.setValueChar('F');
            }else if(radioMap.get(i).equals(String.valueOf(true))){
                record.setValueChar('T');
            }else{
                record.setValueChar(' ');
                record.setValueFloat(Float.parseFloat(radioMap.get(i)));
            }
            record.setId("");
            record.setDeviceId(deviceId);
            record.setPatrolRecordDate(System.currentTimeMillis());
            record.setPatrolContentId(pcList.get(i).getIdd());
            listR.add(record);
        }

        for (Map.Entry<Integer,String> entry: radioMap1.entrySet()
                ) {
            Record record = new Record();
            int i = entry.getKey();
            signPatrol.get(entry.getKey());
            if(radioMap1.get(i).equals(String.valueOf(false))){
                record.setValueChar('F');
            }else if(radioMap1.get(i).equals(String.valueOf(true))){
                record.setValueChar('T');
            }else{
                record.setValueChar(' ');
                record.setValueFloat(Float.parseFloat(radioMap1.get(i)));
            }
            record.setId("");
            record.setDeviceId(deviceId);
            record.setPatrolRecordDate(System.currentTimeMillis());
            record.setPatrolContentId(pcList.get(i).getIdd());
            listR.add(record);
        }

        patrolRecordsPostVo.setRecords(listR);
        patrolRecordsPostVo.setPatrolHeadPageId(patrolHeadPageId);

        String sTemp = patrolRecordsPostVo.toString();
        TaskComponent.commitDetialTask(FullInspectionActivity.this,sTemp);

    }
    @Override
    public void onTaskSuccess(Object obj, EMsgType type, int flag) {

        if(flag == 1){
            SystemUtils.showToast(FullInspectionActivity.this,"提交已成功到服务器！");
        }
        if(flag == 8) {//获取全部巡视作业卡名称
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            String str = jsonObject.get("msg").toString();
            patrolHeadPageId = str.substring(1,str.length()-1);
        }

    }

    @Override
    public void onTaskFailure(Object obj, EMsgType type) {
        SystemUtils.showToast(FullInspectionActivity.this,"失败,服务器或者网络错误");
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getX()-e1.getX() > 88 && Math.abs(velocityX) > 0){
            radioGroup.clearCheck();
            degreeNumber.setFocusable(false);
            secondNumber.setFocusable(false);
            String content = titleContent.getText().toString();
            int point = Integer.parseInt(content.split("\\.")[0]);
            point--;
            if(point>0)
                titleContent.setText(point+"."+map.get(point).substring(1,map.get(point).length()-1));
            if(radioFlag.contains(point)){//单选框
                degree.setVisibility(View.GONE);
                secondDegree.setVisibility(View.GONE);

                radioGroup.setVisibility(View.VISIBLE);
                if(!radioMap.containsKey(point)){//没录入清空
                    radioGroup.clearCheck();
                }else{//已录入 设置
                    if(point<=map.size()&&"true".equals(radioMap.get(point)))
                        radioGroup.check(R.id.radioButton1);
                    if(point<=map.size()&&"false".equals(radioMap.get(point)))
                        radioGroup.check(R.id.radioButton2);
                }
            }
            if(degreeFlag.contains(point)){//填温度数字
                degree.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.GONE);
                degreeNumber.setFocusable(true);
                degreeNumber.setFocusableInTouchMode(true);
                firstPart.setText(pcList.get(point).getPatrolContentName());

                if(signPatrol.containsKey(point)){
                    secondDegree.setVisibility(View.VISIBLE);
                    secondNumber.setFocusable(true);
                    secondNumber.setFocusableInTouchMode(true);
                    secondPart.setText(signPatrol.get(point).getPatrolContentName());
                }
                lastPoint = point;
            }
            if(signFlg.contains(point)){

            }
        } else if(e1.getX() - e2.getX() > 88 && Math.abs(velocityX) > 0){
            radioGroup.clearCheck();
            degreeNumber.setFocusable(false);
            secondNumber.setFocusable(false);
            String content = titleContent.getText().toString();
            int point = Integer.parseInt(content.split("\\.")[0]);
            point++;
            if(point<=map.size())
                titleContent.setText(point+"."+map.get(point).substring(1,map.get(point).length()-1));
            if(radioFlag.contains(point)){//单选框
                degree.setVisibility(View.GONE);
                secondDegree.setVisibility(View.GONE);
                radioGroup.setVisibility(View.VISIBLE);
                if(!radioMap.containsKey(point)){//没录入清空

                }else{//已录入 设置
                    if(point<=map.size()&&"true".equals(radioMap.get(point)))
                        radioGroup.check(R.id.radioButton1);
                    if(point<=map.size()&&"false".equals(radioMap.get(point)))
                        radioGroup.check(R.id.radioButton2);
                }
            }
            if(degreeFlag.contains(point)){//填温度数字

                radioGroup.setVisibility(View.GONE);
                if(radioMap.containsKey(point)){
                    degreeNumber.setText(radioMap.get(point));
                }else{
                    degreeNumber.setText(null);
                }
                if(radioMap1.containsKey(point)){
                    secondNumber.setText(radioMap1.get(point));
                }else{
                    secondNumber.setText(null);
                }
                degree.setVisibility(View.VISIBLE);
                degreeNumber.setFocusable(true);
                degreeNumber.setFocusableInTouchMode(true);
                firstPart.setText(pcList.get(point).getPatrolContentName());
                if(signPatrol.containsKey(point)){
                    secondDegree.setVisibility(View.VISIBLE);
                    secondNumber.setFocusable(true);
                    secondNumber.setFocusableInTouchMode(true);
                    secondPart.setText(signPatrol.get(point).getPatrolContentName());
                }
                lastPoint = point;
            }
            if(signFlg.contains(point)){

            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
