package com.whut.smartinspection.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolTaskDetailDao;
import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.RecordDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.FullWheelAdapter;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.PatrolTaskDetail;
import com.whut.smartinspection.model.PerPatrolCard;
import com.whut.smartinspection.model.Record;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.model.WholePatrolCard;
import com.whut.smartinspection.widgets.CustomToolBar;
import com.whut.smartlibrary.base.SwipeBackActivity;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fortuner on 2018/1/1.
 */

public class FullInspectActivity extends SwipeBackActivity {
    private List<Record> records;
    private List<Record> recordList = new ArrayList<>();
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
    @BindView(R.id.btn_help)
    Button btnHelp;
    List<PatrolTaskDetail> lpatrolNameId;//任务对应的内容
    private FullWheelAdapter wheelAdapter;
    final static String TAG = "FullTourActivity";
    PerPatrolCardDao patrolCardDao = null;
    RecordDao recordDao = null;

    private final ArrayList<DeviceType> deviceTypeList = new ArrayList<>();//设备类型名称列表
    private final ArrayList<String> intervalUnitList = new ArrayList<>();//间隔名称列表
    private final ArrayList<Device> deviceNameList = new ArrayList<>();//设备名称列表
    private String deviceId ;//设备名称ID
    private String deviceName;//设备名称
    private String subIdI;//变电站ID
    private int deviceTypeIddI;//item设备类型ID
    private String deviceTypeNameI;//item设备类型名称
    private TaskItem item;//传过来的任务item
    private String taskId;
    private long deviceDbId = 0L;
    private PerPatrolCard perPatrolCard = null;
    private int pDevice = 0;
    private List<Device> deviceList;
    private long pointPatrol = 0L;
    private WholePatrolCard wholePatrolCard;
    private Long wholeId = 0L;
    private Long perPatrolCardId = 0L;
    private Long recordId = 0L;
    private String patrolNameId;
    private String patrolHeadPageId;
    private List<PatrolContent> patrolContents;
    private List<PatrolContent> patrolContentList = new ArrayList<>();

    private List<Record> resultList ;
    private Map<String,Map<String,List<Record>>> perDeviceType = new HashMap<>();//key-->patrolHeadPageID
    private Map<String,Long> mapId ;
    private Map<String,Map<String,Long>> mapPatrolNameId = new HashMap<>();
    DeviceTypeDao deviceTypedao = SApplication.getInstance().getDaoSession().getDeviceTypeDao();
    DeviceType deviceType;
    DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
    PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
    List<PatrolTaskDetail> lpatrolNameId1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspect);
        ButterKnife.bind(this);
        CustomToolBar.goBack(FullInspectActivity.this);//返回按钮监听
        item = (TaskItem) getIntent().getSerializableExtra("item");
        taskId  = item.getIdd();
        //查询任务对应的细节
        PatrolTaskDetailDao patrolTaskDetailDao = SApplication.getInstance().getDaoSession().getPatrolTaskDetailDao();
        QueryBuilder<PatrolTaskDetail> qbPatrolTDetail = patrolTaskDetailDao.queryBuilder();
        lpatrolNameId1 = qbPatrolTDetail.list();
        lpatrolNameId = qbPatrolTDetail.where(PatrolTaskDetailDao.Properties.TaskId.eq(taskId)).list();
        patrolNameId = lpatrolNameId.size()>0?lpatrolNameId.get(0).getPatrolNameId():null;
//        if(!SApplication.isInitTaskDetail()) {
//            insertData(lpatrolNameId1);//先初始化数据库
//            SApplication.setIsInitTaskDetail(true);
//        }
        insertData(lpatrolNameId1);//先初始化数据库
        initData();
        initView();
    }
    private void insertData(List<PatrolTaskDetail> patrolTaskDetails){
        for (PatrolTaskDetail patrolTaskDetail : patrolTaskDetails){
            patrolNameId = patrolTaskDetail.getPatrolNameId();
            patrolHeadPageId = patrolTaskDetail.getPatrolHeadPageId();
            //提交时按设备类型提交的
            WholePatrolCard wholePatrolCard = new WholePatrolCard(wholeId,patrolHeadPageId,false);
            WholePatrolCardDao wholePatrolCardDao = SApplication.getInstance().getDaoSession().getWholePatrolCardDao();
            wholePatrolCardDao.insertOrReplace(wholePatrolCard);
            //巡视作业卡片内容
            QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
            patrolContents = qbPC.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
            deviceTypeIddI = patrolContents.get(0).getDeviceTypeId();//获得设备类型ID
            QueryBuilder<DeviceType> qbDeviceTpe = deviceTypedao.queryBuilder();
            deviceType =  qbDeviceTpe.where(DeviceTypeDao.Properties.Idd.eq(deviceTypeIddI)).unique();
            if(deviceType!=null)
                deviceTypeList.add(deviceType);
            QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
            deviceList = qbDevice.where(DeviceDao.Properties.DeviceTypeId.eq(deviceTypeIddI)).list();
            patrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
            recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
            mapId =  new HashMap<>();//key -->设备类型iD
            for(int i=0;i<deviceList.size();i++){
                //插入设备
                String devieceId = deviceList.get(i).getIdd();
                PerPatrolCard perPatrolCard = new PerPatrolCard(deviceDbId,devieceId,false,0L,patrolHeadPageId);
                patrolCardDao.insertOrReplace(perPatrolCard);
                mapId.put(devieceId, deviceDbId);
                for(int j = 0;j<patrolContents.size();j++){
                    Record record = new Record();
                    record.setValueChar(" ");
                    record.setValueFloat(0);
                    record.setId(pointPatrol++);
                    record.setFid(deviceDbId);
                    record.setDeviceId(devieceId);
                    record.setPatrolContentId(patrolContents.get(j).getIdd());
                    //加入巡视作业卡id用于提交
                    record.setWholeID(wholeId);
                   recordDao.insertOrReplace(record);
                }
                deviceDbId++;
            }
            wholeId++;
            mapPatrolNameId.put(patrolNameId,mapId);
        }
    }
    private void insertData1(List<PatrolTaskDetail> patrolTaskDetails){
        for (PatrolTaskDetail patrolTaskDetail : patrolTaskDetails){
            patrolNameId = patrolTaskDetail.getPatrolNameId();
            patrolHeadPageId = patrolTaskDetail.getPatrolHeadPageId();
            //巡视作业卡片内容
            QueryBuilder<PatrolContent> qbPC = patrolContentDao.queryBuilder();
            patrolContents = qbPC.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
            deviceTypeIddI = patrolContents.get(0).getDeviceTypeId();//获得设备类型ID
            QueryBuilder<DeviceType> qbDeviceTpe = deviceTypedao.queryBuilder();
            deviceType =  qbDeviceTpe.where(DeviceTypeDao.Properties.Idd.eq(deviceTypeIddI)).unique();
            if(deviceType!=null)
                deviceTypeList.add(deviceType);
            QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
            deviceList = qbDevice.where(DeviceDao.Properties.DeviceTypeId.eq(deviceTypeIddI)).list();
            patrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
            recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
            mapId =  new HashMap<>();//key -->设备类型iD
            PerPatrolCardDao perPatrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
            QueryBuilder<PerPatrolCard> qbPatrolContent = perPatrolCardDao.queryBuilder();
            List<PerPatrolCard> patrolContentTemps = qbPatrolContent.list();
            mapPatrolNameId.put(patrolNameId,mapId);
        }
    }
    private void initData(){
        //转到当前map
        patrolNameId = lpatrolNameId.get(0).getPatrolNameId();
        mapId = mapPatrolNameId.get(patrolNameId);
        //巡视作业卡片内容
        QueryBuilder<PatrolContent> qbPatrolContent = patrolContentDao.queryBuilder();
        patrolContents  = qbPatrolContent.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
        //初始化
        String deviceTypeId = deviceTypeList.get(0).getIdd();
        QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
        qbDevice = deviceDao.queryBuilder();
        deviceList = qbDevice.list();
        deviceList = qbDevice.where(DeviceDao.Properties.DeviceTypeId.eq(Integer.valueOf(deviceTypeId))).list();
        //获取巡视结果容器
        String deviceId = deviceList.get(0).getIdd();
        deviceDbId = mapId.get(deviceId);

        QueryBuilder<Record> qbRecord = recordDao.queryBuilder();
        records = qbRecord.where(RecordDao.Properties.Fid.eq(deviceDbId)).list();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameDis.setText("#1主变本体间隔");
                substationName.setText("锦江变电站500KV");
                deviceTypeName.setText( deviceTypeList.get(0).getName());
                nameDevice.setText(deviceList.get(0).getName());
            }
        });
    }
    private void changeDeviceType(){
        //巡视作业卡片内容
        QueryBuilder<PatrolContent> qbPatrolContent = patrolContentDao.queryBuilder();
        patrolContents  = qbPatrolContent.where(PatrolContentDao.Properties.PatrolNameId.eq(patrolNameId)).list();
        deviceTypeIddI = patrolContents.get(0).getDeviceTypeId();//获得设备类型ID
        QueryBuilder<DeviceType> qbDeviceTpe = deviceTypedao.queryBuilder();
        deviceType =  qbDeviceTpe.where(DeviceTypeDao.Properties.Idd.eq(deviceTypeIddI)).unique();
        //改变设备列表
        QueryBuilder<Device> qbDevice = deviceDao.queryBuilder();
        deviceList = qbDevice.where(DeviceDao.Properties.DeviceTypeId.eq(deviceTypeIddI)).list();
        //改变记录liebiao
        mapId = mapPatrolNameId.get(patrolNameId);
        String deviceId = deviceList.get(0).getIdd();
        deviceDbId = mapId.get(deviceId);
        QueryBuilder<Record> qbRecord = recordDao.queryBuilder();
        records = qbRecord.where(RecordDao.Properties.Fid.eq(deviceDbId)).list();
        patrolContentList.clear();
        for(PatrolContent patrolContent : patrolContents){
            patrolContentList.add(patrolContent);
        }
        recordList.clear();
        for(Record record : records){
            recordList.add(record);
        }
        wheelAdapter.notifyDataSetChanged();

    }

    private void changeDevice(){
        Device device = deviceList.get(pDevice);
        nameDevice.setText(device.getName());
        mapId = mapPatrolNameId.get(patrolNameId);
        deviceDbId = mapId.get(device.getIdd());
        QueryBuilder<Record> qbRecord = recordDao.queryBuilder();
        records = qbRecord.where(RecordDao.Properties.Fid.eq(deviceDbId)).list();
        recordList.clear();
        for(Record record : records){
            recordList.add(record);
        }
        wheelAdapter.notifyDataSetChanged();
    }
    private void initView(){
        for(PatrolContent patrolContent : patrolContents){
            patrolContentList.add(patrolContent);
        }
        for(Record record : records){
            recordList.add(record);
        }
        wheelAdapter = new FullWheelAdapter(this,patrolContentList,recordList);
        final WheelView wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setWheelAdapter(wheelAdapter); // 文本数据源
        wheelView.setSkin(WheelView.Skin.None); // common 皮肤
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
//        style.selectedTextColor = Color.parseColor("#0288ce");
        style.selectedTextColor = R.color.bg_toolbar;
        style.textColor = Color.GRAY;
        style.selectedTextSize = 25;
        style.textSize = 22;
        wheelView.setWheelSize(5);
        wheelView.setStyle(style);
        wheelView.setWheelClickable(true);
        wheelView.setSelection(1);
//        wheelView.setSkin(WheelView.Skin.Holo);

        wheelView.setWheelData(patrolContentList);  // 数据集合
        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                WheelUtils.log("click:" + position);
            }
        });
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<PatrolContent>() {
            @Override
            public void onItemSelected(int position, PatrolContent patrolContent) {
                WheelUtils.log("click:" + position);
                wheelView.getSelectionItem();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入下一设备
                if(pDevice+1 < deviceList.size()) {
                    for(Record record :records){
                        recordDao.insertOrReplace(record);
                    }
                    pDevice++;
                    Device device = deviceList.get(pDevice);
                    nameDevice.setText(device.getName());
                    QueryBuilder<Record> qb = recordDao.queryBuilder();
                    String id = device.getIdd();
                    deviceDbId = mapId.get(id);
                    records = qb.where(RecordDao.Properties.Fid.eq(deviceDbId)).list();
                    wheelAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    @OnClick({R.id.name_device,R.id.style_device,R.id.btn_help})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.name_device:
//                new WheelViewDialog.OnDialogItemClickListener().onItemClick();
                showDialog1(view,deviceList);
                break;
            case R.id.style_device:
                showDialog(view, deviceTypeList);
                break;
            case R.id.btn_help:
                Intent intent = new Intent(FullInspectActivity.this,WorkingHelpActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
    public void showDialog(View view, final List<DeviceType> parent) {
        WheelViewDialog dialog = new WheelViewDialog(this);
        List<String> list = new ArrayList<>();
        for(DeviceType deviceType : parent){
            list.add(deviceType.getName());
        }
        dialog.setTitle("选择设备类型").setItems(list).setButtonText("确定").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(5).setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, String s) {
                int i = position;
                deviceTypeName.setText(parent.get(position).getName());
                pDevice = position;
//                String deviceId = deviceList.get(pDevice).getIdd();
//                deviceDbId = mapId.get(deviceId);
                patrolNameId = lpatrolNameId1.get(position).getPatrolNameId();
                changeDeviceType();
                Log.i(TAG, "onItemClick: "+position);
            }
        }).show();
    }
    public void showDialog1(View view, final List<Device> parent) {
        WheelViewDialog dialog = new WheelViewDialog(this);
        List<String> list = new ArrayList<>();
        for(Device device : parent){
            list.add(device.getName());
        }
        dialog.setTitle("选择设备").setItems(list).setButtonText("确定").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(5).setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, String s) {
                int i = position;
                nameDevice.setText(s);
                pDevice = position;
                for(Record record :recordList){
                    recordDao.insertOrReplace(record);
                }
                changeDevice();
                Log.i(TAG, "onItemClick: "+position);
            }
        }).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        QueryBuilder<Record> qbRecord = recordDao.queryBuilder();
        records = qbRecord.list();
//        records = qbRecord.where(RecordDao)
        Log.i("afterTextChanged", "onPause: ");
        Intent intent = new Intent();
        intent.putExtra("flag","1");
        intent.setAction("com.whut.smartinspection.activity.FullInspectActivity");
        sendBroadcast(intent);
    }
}
