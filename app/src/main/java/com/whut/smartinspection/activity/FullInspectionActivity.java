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

import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolTaskDetailDao;
import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.RecordDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
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
 * 全面巡视页面（第二版）
 * Created by xiongbin on 2017/11/2.
 */
public class FullInspectionActivity extends Activity implements ITaskHandlerListener,GestureDetector.OnGestureListener,View.OnTouchListener {

    @BindView(R.id.rl_gestrue)
    RelativeLayout rlGestrue;
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
    @BindView(R.id.button_id_visual)
    LinearLayout linearLayout;
    private final ArrayList<String> deviceTypeNameList = new ArrayList<>();//设备类型名称列表
    private final ArrayList<String> intervalUnitList = new ArrayList<>();//间隔名称列表
    private final ArrayList<String> deviceNameList = new ArrayList<>();//设备名称列表
    private int point = 0;
    private Map<Integer,String> map = new HashMap<Integer,String>();
    private Map<Integer,String> radioMap = new HashMap<Integer,String>();
    private GestureDetector gestureDetector;
    private String deviceId ;//设备名称ID
    private String deviceName;//设备名称
    private String subIdI;//变电站ID
    private int deviceTypeIddI;//item设备类型ID
    private String deviceTypeNameI;//item设备类型名称
    private TaskItem item;//传过来的任务item
    private String taskId;
    private List<String> patrolNameIDList = new ArrayList<>();
    private List<String> patrolHeadPageIDList = new ArrayList<>();
    private int pointDeviceType = 0;
    private PerPatrolCard perPatrolCard = null;
    private List<Device> deviceList;
    private int devicePoint = 0;
    private WholePatrolCard wholePatrolCard;
    private Long wholeId = 0L;
    private Long perPatrolCardId = 0L;
    private Long recordId = 0L;
    private String patrolNameId;
    private String patrolHeadPageId;
    private List<PatrolContent> patrolContentList;
    private List<Record> resultList ;
    private Map<String,List<Record>> perDeviceTypeRes =  new HashMap<>();
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

//        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        gestureDetector.setIsLongpressEnabled(true);
        rlGestrue.setOnTouchListener(this);
        rlGestrue.setLongClickable(true);

        patrolPerDeviceType();
    }
    private void patrolPerDeviceType(){
        WholePatrolCardDao wholePatrolCardDao = SApplication.getInstance().getDaoSession().getWholePatrolCardDao();
        WholePatrolCard wholePatrolCard = new WholePatrolCard(wholeId,patrolHeadPageId,false);
        wholePatrolCardDao.insertOrReplace(wholePatrolCard);

        deviceNameList.clear();
        deviceTypeNameList.clear();

        //间隔名称
        IntervalUnitDao intervalUnitDao = SApplication.getInstance().getDaoSession().getIntervalUnitDao();
        QueryBuilder<IntervalUnit> qbIU = intervalUnitDao.queryBuilder();
        List<IntervalUnit> listIU = qbIU.list();
        for (IntervalUnit iu : listIU ) {
            intervalUnitList.add(iu.getName());
        }

        //巡视作业卡片内容
        PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
        QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
        patrolContentList = qbPC.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();

        if(patrolContentList.size()>0){
            titleContent.setText(patrolContentList.get(0).getNo()+"."+patrolContentList.get(0).getContent());
        }
        deviceTypeIddI = patrolContentList.get(0).getDeviceTypeId();//获得设备类型ID
        DeviceTypeDao deviceTypeDao = SApplication.getInstance().getDaoSession().getDeviceTypeDao();
        QueryBuilder<DeviceType> dbDeviceType = deviceTypeDao.queryBuilder();
        DeviceType deviceType = dbDeviceType.where(DeviceTypeDao.Properties.Idd.eq(deviceTypeIddI)).unique();
        if(deviceType != null){
            deviceTypeNameI = deviceType.getName();
        }
        //查询设备名称（patrolNameId-->deviceTypeId-->deviceId)
        DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
        QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
        deviceList = qbDevice.where(DeviceDao.Properties.DeviceTypeId.eq(deviceTypeIddI)).list();
        //初始化巡视结果容器
        PerPatrolCardDao perPatrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
        for(int j= 0;j<deviceList.size();j++){
            //插入每个设备
            PerPatrolCard perPatrolCard = new PerPatrolCard(perPatrolCardId,deviceList.get(j).getIdd(),false,wholeId,patrolHeadPageId);
            perPatrolCardDao.insertOrReplace(perPatrolCard);
            resultList = new ArrayList<>();
            for(int i=0;i<patrolContentList.size();i++){
                Record record = new Record();
                record.setId(recordId++);
                record.setDeviceId(deviceList.get(j).getIdd());
                record.setFid(perPatrolCardId);
                record.setPatrolContentId(patrolContentList.get(i).getPatrolNameId());
                resultList.add(i,record);
            }
            perDeviceTypeRes.put(deviceList.get(j).getIdd(),resultList);
            perPatrolCardId ++;
        }
        resultList = perDeviceTypeRes.get(deviceList.get(0).getIdd());//第一个设备
        setSecond(0);
        if(deviceList!=null && deviceList.size()>0){
            deviceId = deviceList.get(0).getIdd();
            deviceName = deviceList.get(0).getName();
        }
        if(deviceNameList.size()>0){//set设备名称
            nameDevice.setText(deviceNameList.get(0));
        }
        //监听获取数据(温度输入）
        degreeNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    resultList.get(point).setValueFloat(Float.valueOf(degreeNumber.getText().toString()));
                    resultList.get(point).setPatrolRecordDate(System.currentTimeMillis());
                }
            }
        });
        //单选
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radioButton1){//√
                    resultList.get(point).setValueChar("T");
                }
                if(checkedId == R.id.radioButton2){//×
                    resultList.get(point).setValueChar("F");
                }
                resultList.get(point).setPatrolRecordDate(System.currentTimeMillis());
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
    }
    @Override
    protected void onPause() {
        super.onPause();
        //退出界面时发起提交服务
        insertData();
    }
    public void insertData(){
        //巡视记录结果(一种设备类型）
        RecordDao recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
        for(int i=0;i<deviceList.size();i++){
            List<Record> temp = perDeviceTypeRes.get(deviceList.get(i).getIdd());
            for(int j=0;j<temp.size();j++){
                if(null == temp.get(j).getValueChar()){
                    temp.get(j).setValueChar(" ");
                }
                recordDao.insertOrReplace(temp.get(j));
            }
        }
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
    public void onTaskFailure(Object obj, EMsgType type,int flag) {
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
    private void setFirst(int point){
        if(point<0){
            return;
        }
        firstTitleContent.setText(patrolContentList.get(point).getNo()+"、"+patrolContentList.get(point).getContent());
        if("0".equals(patrolContentList.get(point).getPatrolContentTypeNo())){//单选框
            firstDegree.setVisibility(View.GONE);
            firstRadioGroup.setVisibility(View.VISIBLE);
            if("T".equals(resultList.get(point).getValueChar())){
                firstRadioGroup.check(R.id.first_radioButton1);
            }else if("F".equals(resultList.get(point).getValueChar())){
                firstRadioGroup.check(R.id.first_radioButton2);
            }else{
                firstRadioGroup.clearCheck();
            }
        }
        if("1".equals(patrolContentList.get(point).getPatrolContentTypeNo())){//填温度数字
            firstRadioGroup.setVisibility(View.GONE);
            firstDegree.setVisibility(View.VISIBLE);
            firstFirPart.setText(patrolContentList.get(point).getPatrolContentName());
            firstDegreeNumber.setText(String.valueOf(resultList.get(point).getValueFloat()));
        }
    }
    private void setThird(int point){
        if(point>=patrolContentList.size()){
            return;
        }
        thirdTitleContent.setText(patrolContentList.get(point).getNo()+"、"+patrolContentList.get(point).getContent());
        if("0".equals(patrolContentList.get(point).getPatrolContentTypeNo())){//单选框
            thirdDegree.setVisibility(View.GONE);
            thirdRadioGroup.setVisibility(View.VISIBLE);
            if("T".equals(resultList.get(point).getValueChar())){
                thirdRadioGroup.check(R.id.third_radioButton1);
            }else if("F".equals(resultList.get(point).getValueChar())){
                thirdRadioGroup.check(R.id.third_radioButton2);
            }else{
                thirdRadioGroup.clearCheck();
            }
        }
        if("1".equals(patrolContentList.get(point).getPatrolContentTypeNo())){//填温度数字
            thirdRadioGroup.setVisibility(View.GONE);
            thirdDegree.setVisibility(View.VISIBLE);
            thirdFirPart.setText(patrolContentList.get(point).getPatrolContentName());
            thirdDegreeNumber.setText(String.valueOf(resultList.get(point).getValueFloat()));
        }
    }
    private void setSecond(int point){
        if(point<0||point>=patrolContentList.size())
            return;
        titleContent.setText(patrolContentList.get(point).getNo() + "." +patrolContentList.get(0).getContent());
        if("0".equals(patrolContentList.get(point).getPatrolContentTypeNo())){
            degree.setVisibility(View.GONE);//隐藏温度输入
            radioGroup.setVisibility(View.VISIBLE);//开启单选框输入

            if("T".equals(resultList.get(point).getValueChar())){
                radioGroup.check(R.id.radioButton1);
            }else if("F".equals(resultList.get(point).getValueChar())){
                radioGroup.check(R.id.radioButton2);
            }else{
                radioGroup.clearCheck();
            }
        }
        if("1".equals(patrolContentList.get(point).getPatrolContentTypeNo())){
            radioGroup.setVisibility(View.GONE);//隐藏单选
            degree.setVisibility(View.VISIBLE);//开启温度输入
            degreeNumber.setFocusable(true);
            degreeNumber.setFocusableInTouchMode(true);
            firPart.setText(patrolContentList.get(point).getPatrolContentName());
            degreeNumber.setText(String.valueOf(resultList.get(point).getValueFloat()));
        }
    }
@Override
public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    if(e2.getY()-e1.getY() > 12 && Math.abs(velocityY) > 0){
        radioGroup.clearCheck();
        degreeNumber.setFocusable(false);
        if(point>0) {
            point--;
           setSecond(point);
        }else{
                devicePoint --;
                if(devicePoint>=0){
                    //返回上个设备
                    resultList = perDeviceTypeRes.get(deviceList.get(devicePoint).getIdd());
                    point = patrolContentList.size()-1;
                    deviceName = deviceList.get(devicePoint).getName();
                    deviceId = deviceList.get(devicePoint).getIdd();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            titleContent.setText(patrolContentList.get(point).getNo() + "." + patrolContentList.get(0).getContent());
                            nameDevice.setText(deviceName);
                        }
                    });
                }else{//第一项
                    devicePoint = 0;
                    point = 0;
                    SystemUtils.showToast(getApplicationContext(),"已经是第一项了");
                }
        }
    } else if(e1.getY() - e2.getY() > 12 && Math.abs(velocityY) > 0){
        radioGroup.clearCheck();
        degreeNumber.setFocusable(false);
        if(point<patrolContentList.size()) {
            point++;
            setSecond(point);
        }else{
            devicePoint ++;
            if(deviceList.size()>devicePoint){
                resultList = perDeviceTypeRes.get(deviceList.get(devicePoint).getIdd());
                point = 0;
                deviceName = deviceList.get(devicePoint).getName();
                deviceId = deviceList.get(devicePoint).getIdd();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nameDevice.setText(deviceName);
                        titleContent.setText(patrolContentList.get(point).getNo() + "." + patrolContentList.get(point).getContent());
                    }
                });
            }else{
                //进入下一个设备类型
                pointDeviceType ++;
                if(pointDeviceType<patrolNameIDList.size()){
                    insertData();
                    patrolNameId = patrolNameIDList.get(pointDeviceType);
                    patrolHeadPageId = patrolHeadPageIDList.get(pointDeviceType);
                    wholeId++;//手动增长
                    patrolPerDeviceType();
                    devicePoint = 0;
                    point = 0;
                }else {
                    devicePoint = deviceList.size() - 1;
                    point = patrolContentList.size()-1;
                    insertData();
                    SystemUtils.showToast(getApplicationContext(),"巡视完成");
                }
            }
        }
    }
    setFirst(point-1);//设置第一个文本框
    setThird(point+1);//设置第二个文本框
    return false;
}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
