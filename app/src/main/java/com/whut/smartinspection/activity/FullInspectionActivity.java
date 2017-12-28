package com.whut.smartinspection.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolTaskDetailDao;
import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.RecordDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.smartinspection.adapters.PageViewTabAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.PatrolTaskDetail;
import com.whut.smartinspection.model.PerPatrolCard;
import com.whut.smartinspection.model.Record;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.model.WholePatrolCard;
import com.whut.smartinspection.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.whut.smartinspection.R;
import com.whut.smartinspection.widgets.CustomToolBar;

import org.greenrobot.greendao.query.QueryBuilder;


//import static com.baidu.location.h.j.R;

/**
 * 全面巡视页面
 * Created by xiongbin on 2017/11/2.
 */
public class FullInspectionActivity extends Activity implements ITaskHandlerListener,GestureDetector.OnGestureListener,View.OnTouchListener {

    @BindView(R.id.rl_gestrue)
    RelativeLayout rlGestrue;
//    @BindView(R.id.toolbar_right_tv)
//    TextView tvCommit;
    @BindView(R.id.tv_patrol_card_name)
    TextView tvPatrolCardName;
    @BindView(R.id.fir_part)
    TextView firPart;
    @BindView(R.id.second_part)
    TextView secondPart;
    @BindView(R.id.degree_number)
    EditText degreeNumber;
    @BindView(R.id.secod_degree_number)
    EditText secondNumber;
    @BindView(R.id.title_content)
    TextView titleContent;//标题题目
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.degree_full)
    LinearLayout degree;
    @BindView(R.id.second_degree_full)
    LinearLayout secondDegree;
//第一个展示框
    @BindView(R.id.first_tv_patrol_card_name)
    TextView firstTvPatrolCardName;
    @BindView(R.id.first_fir_part)
    TextView firstFirPart;
    @BindView(R.id.first_second_part)
    TextView firstSecondPart;
    @BindView(R.id.first_degree_number)
    EditText firstDegreeNumber;
    @BindView(R.id.first_secod_degree_number)
    EditText firstSecondNumber;
    @BindView(R.id.first_title_content)
    TextView firstTitleContent;//标题题目
    @BindView(R.id.first_radioGroup)
    RadioGroup firstRadioGroup;
    @BindView(R.id.first_degree_full)
    LinearLayout firstDegree;
    @BindView(R.id.first_second_degree_full)
    LinearLayout firstSecondDegree;

//第三个展示框
    @BindView(R.id.third_tv_patrol_card_name)
    TextView thirdTvPatrolCardName;
    @BindView(R.id.third_fir_part)
    TextView thirdFirPart;
    @BindView(R.id.third_second_part)
    TextView thirdSecondPart;
    @BindView(R.id.third_degree_number)
    EditText thirdDegreeNumber;
    @BindView(R.id.third_secod_degree_number)
    EditText thirdSecondNumber;
    @BindView(R.id.third_title_content)
    TextView thirdTitleContent;//标题题目
    @BindView(R.id.third_radioGroup)
    RadioGroup thirdRadioGroup;
    @BindView(R.id.third_degree_full)
    LinearLayout thirdDegree;
    @BindView(R.id.third_second_degree_full)
    LinearLayout thirdSecondDegree;

//    @BindView(R.id.tv_full_inspection_back)
//    TextView tvFullInspectionBack; // 返回
    @BindView(R.id.style_device)
    EditText deviceTypeName;  //设备类型
    @BindView(R.id.substation_name)
    EditText substationName;
    @BindView(R.id.name_device)
    EditText nameDevice;//设备名称
    @BindView(R.id.name_dis)
    EditText nameDis ;//间隔名称
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.vp_view)
    ViewPager mViewPager;
//    @BindView(R.id.scorll_view)
//    ScrollView scrollView;
    @BindView(R.id.button_id_visual)
    LinearLayout linearLayout;


    private EditText content1;
    private EditText content2;
    private EditText content3;
    private final ArrayList<String> deviceTypeNameList = new ArrayList<>();//设备类型名称列表
    private final ArrayList<String> intervalUnitList = new ArrayList<>();//间隔名称列表
    private final ArrayList<String> deviceNameList = new ArrayList<>();//设备名称列表
    private List<Record>patrolConResList = new ArrayList<>();//巡视项目结果列表
    private int point = 1;
    private Map<Integer,String> map = new HashMap<Integer,String>();
    private Map<Integer,String> radioMap = new HashMap<Integer,String>();
    private Map<Integer,String> radioMap1 = new HashMap<Integer,String>();
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
    private Map<String,Long> deviceIdMapInerId = new HashMap<>();
//    private String patrolHeadPageId ;//首页ID
    private String patrolContentId ;//巡视作业卡名字ID
    private String deviceId ;//设备名称ID
    private String deviceName;//设备名称
    private String subIdI;//变电站ID
    private int deviceTypeIddI;//item设备类型ID
    private String deviceTypeNameI;//item设备类型名称
    private TaskItem item;//传过来的任务item
//    private String patrolNameId;
    private String taskId;
    private List<String> patrolNameIDList = new ArrayList<>();
    private List<String> patrolHeadPageIDList = new ArrayList<>();
    private int pointDeviceType = 0;
    private PerPatrolCard perPatrolCard = null;
    private List<Device> deviceList;
    private int devicePoint = 0;
    private WholePatrolCard wholePatrolCard;
    Long wholeId = null;
    String patrolNameId;
    String patrolHeadPageId;
    public FullInspectionActivity(){
        gestureDetector = new GestureDetector(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspection);
        ButterKnife.bind(this);
        CustomToolBar.goBack(FullInspectionActivity.this);//返回按钮监听
        item = (TaskItem) getIntent().getSerializableExtra("item");
        taskId  = item.getIdd();
        //查询任务对应的细节
        PatrolTaskDetailDao patrolTaskDetailDao = SApplication.getInstance().getDaoSession().getPatrolTaskDetailDao();
        QueryBuilder<PatrolTaskDetail> qbPatrolTDetail = patrolTaskDetailDao.queryBuilder();
        List<PatrolTaskDetail> lpatrolNameId1 = qbPatrolTDetail.list();
        List<PatrolTaskDetail> lpatrolNameId = qbPatrolTDetail.where(PatrolTaskDetailDao.Properties.TaskId.eq(taskId)).list();
        patrolNameId = lpatrolNameId.size()>0?lpatrolNameId.get(0).getPatrolNameId():null;
        for (PatrolTaskDetail temp:lpatrolNameId){
            String tPatrolNameId = temp.getPatrolNameId();
            patrolNameIDList.add(tPatrolNameId);
        }

        patrolHeadPageId = lpatrolNameId.size()>0?lpatrolNameId.get(0).getPatrolHeadPageId():null;
        for(PatrolTaskDetail temp:lpatrolNameId){
            String tpatrolHeadPageId = temp.getPatrolHeadPageId();
            patrolHeadPageIDList.add(tpatrolHeadPageId);
        }
        initView();
        patrolPerDeviceType();
//        initTabView();
    }
    private void patrolPerDeviceType(){
        map.clear();//清空数据
        pcList.clear();
        radioFlag.clear();
        radioMap.clear();
        radioMap1.clear();
        deviceNameList.clear();
        patrolConResList.clear();
        deviceTypeNameList.clear();
        intervalUnitList.clear();
        //间隔名称
        IntervalUnitDao intervalUnitDao = SApplication.getInstance().getDaoSession().getIntervalUnitDao();
        QueryBuilder<IntervalUnit> qbIU = intervalUnitDao.queryBuilder();
        List<IntervalUnit> listIU = qbIU.list();
        for (IntervalUnit iu   :listIU ) {
            intervalUnitList.add(iu.getName());
        }

        //1.插入一个巡视作业卡
        WholePatrolCard wholePatrolCard = new WholePatrolCard(null,patrolHeadPageId,false);
        WholePatrolCardDao wholePatrolCardDao = SApplication.getInstance().getDaoSession().getWholePatrolCardDao();
        wholePatrolCardDao.insertOrReplace(wholePatrolCard);
        wholeId = wholePatrolCard.getId();
        //巡视作业卡片内容
        PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
        QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
        List<PatrolContent> listPC = qbPC.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
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
        }
        deviceTypeIddI = listPC.get(0).getDeviceTypeId();//获得设备类型ID
        DeviceTypeDao deviceTypeDao = SApplication.getInstance().getDaoSession().getDeviceTypeDao();
        QueryBuilder<DeviceType> dbDeviceType = deviceTypeDao.queryBuilder();
        DeviceType deviceType = dbDeviceType.where(DeviceTypeDao.Properties.Idd.eq(deviceTypeIddI)).unique();
        if(deviceType != null){
            deviceTypeNameI = deviceType.getName();
        }
        if(intervalUnitList.size()>0){//set间隔名称
            nameDis.setText(intervalUnitList.get(0));
        }
        //查询设备名称（patrolNameId-->deviceTypeId-->deviceId)
        DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
        QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
        List<Device> ll = qbDevice.list();
        deviceList = qbDevice.where(DeviceDao.Properties.DeviceTypeId.eq(deviceTypeIddI)).list();
        if(deviceList!=null && deviceList.size()>0){
            deviceId = deviceList.get(0).getIdd();
            deviceName = deviceList.get(0).getName();
        }

        if(deviceNameList.size()>0){//set设备名称
            nameDevice.setText(deviceNameList.get(0));
        }
        degreeNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    radioMap.put(point,degreeNumber.getText().toString());
                }
            }
        });
        secondNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    radioMap1.put(point,secondNumber.getText().toString());
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameDis.setText(intervalUnitList.get(0));
                substationName.setText("锦江变电站500KV");
                nameDevice.setText(deviceName);
                deviceTypeName.setText(deviceTypeNameI);
            }
        });
    }
    private void initView(){
//        OverScrollDecoratorHelper.setUpOverScroll(scrollView);//滑动样式
        gestureDetector.setIsLongpressEnabled(true);//左右滑
        rlGestrue.setOnTouchListener(this);
        rlGestrue.setLongClickable(true);
    }

    public void initTabView(){

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
                }
            }
        });
        content2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String s = content2.getText().toString();
                }
            }
        });
        content3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String s = content3.getText().toString();
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
    }
    @OnClick({R.id.toolbar_right_tv,R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_tv:
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
            case R.id.btn_help:
                Intent intent = new Intent(FullInspectionActivity.this,WorkingHelpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void commitData(){
        perPatrolCard = new PerPatrolCard();
        for (Map.Entry<Integer,String> entry: radioMap.entrySet()
                ) {
            Record record = new Record();
            int i = entry.getKey();

            if(radioMap.get(i).equals(String.valueOf(false))){
                record.setValueChar("F");
            }else if(radioMap.get(i).equals(String.valueOf(true))){
                record.setValueChar("T");
            }else{
                record.setValueChar(" ");
                record.setValueFloat(Float.parseFloat(radioMap.get(i)));
            }
            record.setIdd("");
            record.setDeviceId(deviceId);
            record.setPatrolRecordDate(System.currentTimeMillis());
            record.setPatrolContentId(pcList.get(i).getIdd());
            patrolConResList.add(record);
        }

        for (Map.Entry<Integer,String> entry: radioMap1.entrySet()
                ) {
            Record record = new Record();
            int i = entry.getKey();
            signPatrol.get(entry.getKey());
            if(radioMap1.get(i).equals(String.valueOf(false))){
                record.setValueChar("F");
            }else if(radioMap1.get(i).equals(String.valueOf(true))){
                record.setValueChar("T");
            }else{
                record.setValueChar(" ");
                record.setValueFloat(Float.parseFloat(radioMap1.get(i)));
            }
            record.setIdd("");
            record.setDeviceId(deviceId);
            record.setPatrolRecordDate(System.currentTimeMillis());
            record.setPatrolContentId(pcList.get(i).getIdd());
            patrolConResList.add(record);
        }
        perPatrolCard.setRecords(patrolConResList);
//        perPatrolCard.setDeviceId(patrolHeadPageId);
        String sTemp = perPatrolCard.toString();
        TaskComponent.commitDetialTask(FullInspectionActivity.this,sTemp);

    }
    @Override
    protected void onPause() {
        super.onPause();
        //退出界面时发起提交服务
        insertData();
    }
    public void insertData(){
        //插入一个设备的巡视记录结果
        PerPatrolCardDao perPatrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
        Long perInsertId = null ;
        if(!deviceIdMapInerId.containsKey(deviceId)){//生成id
            perPatrolCard = new PerPatrolCard(null,deviceId,false,wholeId,patrolHeadPageId);
            perPatrolCardDao.insertOrReplace(perPatrolCard);
            deviceIdMapInerId.put(deviceId,perPatrolCard.getId());
            perInsertId = perPatrolCard.getId();
        }else{//id已存在
            perInsertId = deviceIdMapInerId.get(deviceId);
            QueryBuilder<PerPatrolCard> qbPer = perPatrolCardDao.queryBuilder();
            PerPatrolCard per = qbPer.where(PerPatrolCardDao.Properties.Id.eq(perInsertId)).unique();
            List<PerPatrolCard> pper = qbPer.where(PerPatrolCardDao.Properties.Id.eq(perInsertId)).list();
            perPatrolCard = per ;
        }

        Long fid = perPatrolCard.getId();
        for (Map.Entry<Integer,String> entry: radioMap.entrySet()
                ) {
            Record record = new Record();
            int i = entry.getKey();

            if(radioMap.get(i).equals(String.valueOf(false))){
                record.setValueChar("F");
            }else if(radioMap.get(i).equals(String.valueOf(true))){
                record.setValueChar("T");
            }else{
                record.setValueChar(" ");
                record.setValueFloat(Float.parseFloat(radioMap.get(i)));
            }
            String recordId = pcList.get(i).getIdd();
            record.setIdd("");
            record.setDeviceId(deviceId);
            record.setPatrolRecordDate(System.currentTimeMillis());
            record.setPatrolContentId(recordId);
            record.setFid(fid);
            RecordDao recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
            QueryBuilder<Record> qbRec = recordDao.queryBuilder();
            List<Record> l = qbRec.list();
            List<Record> ll = qbRec.where(RecordDao.Properties.Idd.eq(recordId)).list();
            if(ll.size()>0){
                record.setId(ll.get(0).getId());
            }
            recordDao.insertOrReplace(record);
        }

        for (Map.Entry<Integer,String> entry: radioMap1.entrySet()
                ) {
            Record record = new Record();
            int i = entry.getKey();
            signPatrol.get(entry.getKey());
            if(radioMap1.get(i).equals(String.valueOf(false))){
                record.setValueChar("F");
            }else if(radioMap1.get(i).equals(String.valueOf(true))){
                record.setValueChar("T");
            }else{
                record.setValueChar(" ");
                record.setValueFloat(Float.parseFloat(radioMap1.get(i)));
            }
            String recordId = pcList.get(i).getIdd();
            record.setIdd("");
            record.setDeviceId(deviceId);
            record.setPatrolRecordDate(System.currentTimeMillis());
            record.setPatrolContentId(pcList.get(i).getIdd());
            record.setFid(fid);
            RecordDao recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
            QueryBuilder<Record> qbRec = recordDao.queryBuilder();
            List<Record> l = qbRec.list();
            List<Record> ll = qbRec.where(RecordDao.Properties.Idd.eq(recordId)).list();
            if(ll.size()>0){
                record.setId(ll.get(0).getId());
            }
            recordDao.insertOrReplace(record);
        }
        //发消息给HomePageActivity
        Intent intent = new Intent();
        intent.putExtra("flag","1");
        intent.setAction("com.whut.smartinspection.activity.FullInspectionActivity");
        sendBroadcast(intent);
    }
    @Override
    public void onTaskSuccess(Object obj, EMsgType type, int flag) {
        if(flag == 1){
            SystemUtils.showToast(FullInspectionActivity.this,"提交已成功到服务器！");
        }
//        if(flag == 8) {//获取全部巡视作业卡名称
//            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
//            String str = jsonObject.get("msg").toString();
//            patrolHeadPageId = str.substring(1,str.length()-1);
//        }
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
        if(e2.getY()-e1.getY() > 12 && Math.abs(velocityY) > 0){
            radioGroup.clearCheck();
            degreeNumber.setFocusable(false);
            secondNumber.setFocusable(false);
//            firstTitleContent.setText(point + "." + pcList.get(point).getContent());
//            point --;
            if(point-1>0) {//point在边界之内
                thirdTitleContent.setText(point + "." + pcList.get(point).getContent());
                setThirdItemRadioGroup(point);
                point--;
                if(point-1>0){
                    firstTitleContent.setText(point-1 + "." + pcList.get(point-1).getContent());
                    setFirstItemRadioGroup(point-1);
                }else{
                    firstTitleContent.setText("空白");
                    setFirstItemRadioGroup(0);
                }
                titleContent.setText(point + "." + pcList.get(point).getContent());
                if(radioFlag.contains(point)){
                    degree.setVisibility(View.GONE);//隐藏温度输入
                    secondDegree.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);//开启单选框输入
                    if(!radioMap.containsKey(point)){//没录入清空
                        radioGroup.clearCheck();
                    }else{//已录入 设置
                        if(point<=map.size()&&"true".equals(radioMap.get(point)))
                            radioGroup.check(R.id.radioButton1);
                        if(point<=map.size()&&"false".equals(radioMap.get(point)))
                            radioGroup.check(R.id.radioButton2);
                    }
                }
                if(degreeFlag.contains(point)){
                    radioGroup.setVisibility(View.GONE);
                    degree.setVisibility(View.VISIBLE);//开启温度输入
                    degreeNumber.setFocusable(true);
                    degreeNumber.setFocusableInTouchMode(true);
                    if(radioMap.containsKey(point)){
                        degreeNumber.setText(radioMap.get(point));
                    }else{
                        degreeNumber.setText(null);
                    }
                    firPart.setText(pcList.get(point).getPatrolContentName());

                    if(signPatrol.containsKey(point)){//第二次次输入温度
                        secondDegree.setVisibility(View.VISIBLE);
                        secondNumber.setFocusable(true);
                        secondNumber.setFocusableInTouchMode(true);
                        secondPart.setText(signPatrol.get(point).getPatrolContentName());
                        if(radioMap1.containsKey(point)){
                            secondNumber.setText(radioMap1.get(point));
                        }else{
                            secondNumber.setText(null);
                        }
                    }
                }
                if(signFlg.contains(point)){
                }
            }else{
                devicePoint --;
                if(devicePoint>=0){
                    insertData();//进入下一个设备之前 插入上一个设备的巡视结果
                    thirdTitleContent.setText(point + "." + pcList.get(point).getContent());
                    setThirdItemRadioGroup(point);
                    point = map.size();
                    if(pcList.size()-1>0) {
                        firstTitleContent.setText(point-1 + "." + pcList.get(point - 1).getContent());
                        setFirstItemRadioGroup(point-1);
                    }else{
                        firstTitleContent.setText("已经是第一页了");
                        setFirstItemRadioGroup(0);
                    }
                    deviceName = deviceList.get(devicePoint).getName();
                    deviceId = deviceList.get(devicePoint).getIdd();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            titleContent.setText(point + "." + pcList.get(point).getContent());
                            nameDevice.setText(deviceName);
                        }
                    });
                }else{//第一项
                    devicePoint = 0;
                    point = 1;
                    SystemUtils.showToast(getApplicationContext(),"已经是第一项了");
                }
            }
        } else if(e1.getY() - e2.getY() > 12 && Math.abs(velocityY) > 0){
            radioGroup.clearCheck();
            degreeNumber.setFocusable(false);
            secondNumber.setFocusable(false);
//            point++;
            if(point+1<=map.size()) {
                firstTitleContent.setText(point + "." + pcList.get(point).getContent());
                setFirstItemRadioGroup(point);
                point++;
                if(point+1<=pcList.size()){
                    thirdTitleContent.setText(point+1 + "." + pcList.get(point+1).getContent());
                    setThirdItemRadioGroup(point+1);
                }else{
                    thirdTitleContent.setText("最后一页");
                    setThirdItemRadioGroup(point+1);
                }
                titleContent.setText(point + "." + pcList.get(point).getContent());
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
                    radioGroup.setVisibility(View.GONE);
                    degree.setVisibility(View.VISIBLE);
                    degreeNumber.setFocusable(true);
                    degreeNumber.setFocusableInTouchMode(true);
                    firPart.setText(pcList.get(point).getPatrolContentName());
                    if(radioMap.containsKey(point)){
                        degreeNumber.setText(radioMap.get(point));
                    }else{
                        degreeNumber.setText(null);
                    }
                    if(signPatrol.containsKey(point)){
                        secondDegree.setVisibility(View.VISIBLE);
                        secondNumber.setFocusable(true);
                        secondNumber.setFocusableInTouchMode(true);
                        secondPart.setText(signPatrol.get(point).getPatrolContentName());
                        if(radioMap1.containsKey(point)){
                            secondNumber.setText(radioMap1.get(point));
                        }else{
                            secondNumber.setText(null);
                        }
                    }
                }
                if(signFlg.contains(point)){
                }
            }else{
                devicePoint ++;
                if(deviceList.size()>devicePoint){
                    insertData();//进入下一个设备之前 插入上一个设备的巡视结果
                    //设置第一个框
                    firstTitleContent.setText(point + "." + pcList.get(point).getContent());
                    setFirstItemRadioGroup(point);
                    point = 1;
                    if(point+1<=pcList.size()) {
                        thirdTitleContent.setText(point+1 + "." + pcList.get(point+1).getContent());
                        setThirdItemRadioGroup(point+1);
                    }else{
                        thirdTitleContent.setText("暂无数据");
                        setThirdItemRadioGroup(point+1);
                    }
                    deviceName = deviceList.get(devicePoint).getName();
                    deviceId = deviceList.get(devicePoint).getIdd();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameDevice.setText(deviceName);
                            titleContent.setText(point + "." + pcList.get(point).getContent());
                        }
                    });
                }else{
                    pointDeviceType ++;
                    if(pointDeviceType<patrolNameIDList.size()){
                        patrolNameId = patrolNameIDList.get(pointDeviceType);
                        patrolHeadPageId = patrolHeadPageIDList.get(pointDeviceType);
                        patrolPerDeviceType();
                        devicePoint = 0;
                        point = 1;
                        insertData();
                    }else {
                        devicePoint = deviceList.size() - 1;
                        point = map.size();
                        insertData();
                        SystemUtils.showToast(getApplicationContext(),"巡视完成");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    public void setFirstItemRadioGroup(int  point){
        if(point<=0){
            firstDegree.setVisibility(View.GONE);
            firstSecondDegree.setVisibility(View.GONE);
            firstRadioGroup.setVisibility(View.GONE);
            return;
        }
        if(radioFlag.contains(point)){//单选框
            firstDegree.setVisibility(View.GONE);
            firstSecondDegree.setVisibility(View.GONE);
            firstRadioGroup.setVisibility(View.VISIBLE);
            if(!radioMap.containsKey(point)){//没录入清空
                firstRadioGroup.clearCheck();
            }else{//已录入 设置
                if(point<=map.size()&&"true".equals(radioMap.get(point)))
                    firstRadioGroup.check(R.id.first_radioButton1);
                if(point<=map.size()&&"false".equals(radioMap.get(point)))
                    firstRadioGroup.check(R.id.first_radioButton2);
            }
        }
        if(degreeFlag.contains(point)){//填温度数字
            firstRadioGroup.setVisibility(View.GONE);
            firstDegree.setVisibility(View.VISIBLE);
//            firstDegreeNumber.setFocusable(true);
//            firstDegreeNumber.setFocusableInTouchMode(true);
            firstFirPart.setText(pcList.get(point).getPatrolContentName());
            if(radioMap.containsKey(point)){
                firstDegreeNumber.setText(radioMap.get(point));
            }else{
                firstDegreeNumber.setText(null);
            }
            if(signPatrol.containsKey(point)){
                firstSecondDegree.setVisibility(View.VISIBLE);
//                firstSecondNumber.setFocusable(true);
//                firstSecondNumber.setFocusableInTouchMode(true);
                firstSecondPart.setText(signPatrol.get(point).getPatrolContentName());
                if(radioMap1.containsKey(point)){
                    firstSecondNumber.setText(radioMap1.get(point));
                }else{
                    firstSecondNumber.setText(null);
                }
            }
        }
        if(signFlg.contains(point)){
        }
    }
    public void setThirdItemRadioGroup(int  point){
        if(point>pcList.size()){
            thirdDegree.setVisibility(View.GONE);
            thirdSecondDegree.setVisibility(View.GONE);
            thirdRadioGroup.setVisibility(View.GONE);
            return;
        }
        if(radioFlag.contains(point)){//单选框
            thirdDegree.setVisibility(View.GONE);
            thirdSecondDegree.setVisibility(View.GONE);
            thirdRadioGroup.setVisibility(View.VISIBLE);
            if(!radioMap.containsKey(point)){//没录入清空
                thirdRadioGroup.clearCheck();
            }else{//已录入 设置
                if(point<=map.size()&&"true".equals(radioMap.get(point)))
                    thirdRadioGroup.check(R.id.third_radioButton1);
                if(point<=map.size()&&"false".equals(radioMap.get(point)))
                    thirdRadioGroup.check(R.id.third_radioButton2);
            }
        }
        if(degreeFlag.contains(point)){//填温度数字
            thirdRadioGroup.setVisibility(View.GONE);
            thirdDegree.setVisibility(View.VISIBLE);
//            thirdDegreeNumber.setFocusable(true);
//            thirdDegreeNumber.setFocusableInTouchMode(true);
            thirdFirPart.setText(pcList.get(point).getPatrolContentName());
            if(radioMap.containsKey(point)){
                thirdDegreeNumber.setText(radioMap.get(point));
            }else{
                thirdDegreeNumber.setText(null);
            }
            if(signPatrol.containsKey(point)){
                thirdSecondDegree.setVisibility(View.VISIBLE);
//                firstSecondNumber.setFocusable(true);
//                firstSecondNumber.setFocusableInTouchMode(true);
                thirdSecondPart.setText(signPatrol.get(point).getPatrolContentName());
                if(radioMap1.containsKey(point)){
                    thirdSecondNumber.setText(radioMap1.get(point));
                }else{
                    thirdSecondNumber.setText(null);
                }
            }
        }
        if(signFlg.contains(point)){
        }
    }
}
