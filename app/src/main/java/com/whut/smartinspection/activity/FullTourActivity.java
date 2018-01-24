package com.whut.smartinspection.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolTaskDetailDao;
import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.FullTourPatrolAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EventCallable;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.PatrolTaskDetail;
import com.whut.smartinspection.model.PerPatrolCard;
import com.whut.smartinspection.model.Record;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.model.WholePatrolCard;
import com.whut.smartlibrary.base.SwipeBackActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2017/12/28.
 * 全面巡视  第一版（改进版本)
 */

public class FullTourActivity extends SwipeBackActivity implements AdapterView.OnItemClickListener,EventCallable {
    private List<Record> records;
    @BindView(R.id.lv_full_tour)
    ListView listView;
    @BindView(R.id.style_device)
    EditText deviceTypeName;  //设备类型
    @BindView(R.id.substation_name)
    EditText substationName;
    @BindView(R.id.name_device)
    EditText nameDevice;//设备名称
    @BindView(R.id.name_dis)
    EditText nameDis ;//间隔名称
    @BindView(R.id.btn_next)
    Button btnNext;
    List<PatrolTaskDetail> lpatrolNameId;//任务对应的内容
    private FullTourPatrolAdapter fullTourPatrolAdapter;
    final static String TAG = "FullTourActivity";

    private final ArrayList<String> deviceTypeNameList = new ArrayList<>();//设备类型名称列表
    private final ArrayList<String> intervalUnitList = new ArrayList<>();//间隔名称列表
    private final ArrayList<String> deviceNameList = new ArrayList<>();//设备名称列表
    private String deviceId ;//设备名称ID
    private String deviceName;//设备名称
    private String subIdI;//变电站ID
    private int deviceTypeIddI;//item设备类型ID
    private String deviceTypeNameI;//item设备类型名称
    private TaskItem item;//传过来的任务item
    private String taskId;
    private int pointDeviceType = 0;
    private PerPatrolCard perPatrolCard = null;
    private List<Device> deviceList;
    private int pointDevice = 0;
    private WholePatrolCard wholePatrolCard;
    private Long wholeId = 0L;
    private Long perPatrolCardId = 0L;
    private Long recordId = 0L;
    private String patrolNameId;
    private String patrolHeadPageId;
    private List<PatrolContent> patrolContents;
    private List<Record> resultList ;
    private Map<String,Map<String,List<Record>>> perDeviceType = new HashMap<>();//key-->patrolHeadPageID
    private Map<String,List<Record>> perDeviceRes =  new HashMap<>();//key -->设备类iD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_tour);
        ButterKnife.bind(this);
        item = (TaskItem) getIntent().getSerializableExtra("item");
        taskId  = item.getIdd();
        //查询任务对应的细节
        PatrolTaskDetailDao patrolTaskDetailDao = SApplication.getInstance().getDaoSession().getPatrolTaskDetailDao();
        QueryBuilder<PatrolTaskDetail> qbPatrolTDetail = patrolTaskDetailDao.queryBuilder();
        List<PatrolTaskDetail> lpatrolNameId1 = qbPatrolTDetail.list();
        lpatrolNameId = qbPatrolTDetail.where(PatrolTaskDetailDao.Properties.TaskId.eq(taskId)).list();
        patrolNameId = lpatrolNameId.size()>0?lpatrolNameId.get(0).getPatrolNameId():null;
        patrolHeadPageId = lpatrolNameId.size()>0?lpatrolNameId.get(0).getPatrolHeadPageId():null;

//        for(int i=0;i<lpatrolNameId1.size();i++){
//            patrolNameId = lpatrolNameId.size()>i?lpatrolNameId.get(i).getPatrolNameId():null;
//            patrolHeadPageId = lpatrolNameId.size()>i?lpatrolNameId.get(i).getPatrolHeadPageId():null;
//            initPerDeviceType(patrolHeadPageId,patrolNameId);
//        }

        initPerDevice(patrolHeadPageId,patrolNameId);
        initView();
    }
    private void initView(){
        fullTourPatrolAdapter = new FullTourPatrolAdapter(this,patrolContents,records,this);
        listView.setAdapter(fullTourPatrolAdapter);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入下一设备
//                records = perDeviceType.get("").get("");
            }
        });
    }
    private void initPerDevice(String patrolHeadPageId,String patrolNameId){
        //巡视作业卡片内容
        PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
        QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
        patrolContents = qbPC.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
        records = new ArrayList<>();
        for(int i = 0;i<patrolContents.size();i++){
            Record record = new Record();
            record.setValueChar(null);
            record.setValueFloat(0);
            records.add(record);
        }

//        resultList = perDeviceRes.get(deviceList.get(0).getIdd());//第一个设备
//        if(deviceList!=null && deviceList.size()>0){
//            deviceId = deviceList.get(0).getIdd();
//            deviceName = deviceList.get(0).getName();
//        }
//        if(deviceNameList.size()>0){//set设备名称
//            nameDevice.setText(deviceNameList.get(0));
//        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameDis.setText("#1主变本体间隔");
                substationName.setText("锦江变电站500KV");
                nameDevice.setText("主变压器");
                deviceTypeName.setText("500KV主变压器A相");
            }
        });
    }

    private void initPerDeviceType(String patrolHeadPageId,String patrolNameId){
        deviceNameList.clear();
        deviceTypeNameList.clear();

        //巡视作业卡片内容
        PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
        QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
        patrolContents = qbPC.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
        records = new ArrayList<>();
        for(int i = 0;i<patrolContents.size();i++){
            Record record = new Record();
            record.setValueChar(null);
            record.setValueFloat(0);
            records.add(record);
        }

        //初始化巡视作业卡
        WholePatrolCardDao wholePatrolCardDao = SApplication.getInstance().getDaoSession().getWholePatrolCardDao();
        WholePatrolCard wholePatrolCard = new WholePatrolCard(wholeId,patrolHeadPageId,false);
        wholePatrolCardDao.insertOrReplace(wholePatrolCard);

        //间隔名称
        IntervalUnitDao intervalUnitDao = SApplication.getInstance().getDaoSession().getIntervalUnitDao();
        QueryBuilder<IntervalUnit> qbIU = intervalUnitDao.queryBuilder();
        List<IntervalUnit> listIU = qbIU.list();
        for (IntervalUnit iu : listIU ) {
            intervalUnitList.add(iu.getName());
        }
        //获得设备类型ID
        deviceTypeIddI = patrolContents.get(0).getDeviceTypeId();
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
            for(int i = 0; i< patrolContents.size(); i++){
                Record record = new Record();
                record.setId(recordId++);
                record.setDeviceId(deviceList.get(j).getIdd());
                record.setFid(perPatrolCardId);
                record.setPatrolContentId(patrolContents.get(i).getPatrolNameId());
                resultList.add(i,record);
            }
            perDeviceRes.put(deviceList.get(j).getIdd(),resultList);
            perPatrolCardId ++;
        }
        perDeviceType.put(deviceType.getIdd(),perDeviceRes);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onInnerClick(View view) {

        switch (view.getId()){
            case R.id.rg_radioGroup://单选

                break;
            case R.id.et_degree:

                break;
            default:
                break;
        }
    }
}
