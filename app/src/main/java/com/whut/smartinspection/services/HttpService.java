package com.whut.smartinspection.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolTaskDetailDao;
import com.whut.greendao.gen.PatrolWorkCardDao;
import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.RecordDao;
import com.whut.greendao.gen.SubDao;
import com.whut.greendao.gen.TaskItemDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.activity.HomePageActivity;
import com.whut.smartinspection.activity.MyTaskActivity;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.db.BaseDbComponent;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IDetailHandlerListener;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.BaseHttpComponent;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.PatrolTaskDetail;
import com.whut.smartinspection.model.PatrolWorkCard;
import com.whut.smartinspection.model.PerPatrolCard;
import com.whut.smartinspection.model.Record;
import com.whut.smartinspection.model.Sub;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.model.WholePatrolCard;
import com.whut.smartinspection.utils.SystemUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import java.util.logging.Handler;

import cn.com.reformer.rfBleService.BleDevContext;
import cn.com.reformer.rfBleService.BleService;
import cn.com.reformer.rfBleService.OnCompletedListener;
import cn.com.reformer.rfBleService.OnPasswordWriteListener;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Fortuner on 2017/12/5.
 */

public class HttpService extends Service implements ITaskHandlerListener,IDetailHandlerListener{
    private BleService.RfBleKey rfBleKey = null;
    private List<String> data_list = new ArrayList<>();
    SensorManager sensorManager;
    private BleService mService;
    Vibrator vibrator;
    private ShakeListener shakeListener;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,IBinder rawBinder) {
            mService = ((BleService.LocalBinder) rawBinder).getService();
            rfBleKey = mService.getRfBleKey();
            rfBleKey.init(null);//mWhiteList
            rfBleKey.setOnCompletedListener(new OnCompletedListener() {
                @Override
                public void OnCompleted(byte[] bytes, int i) {
                    final int result = i;
                    switch (result) {
                        case 0://成功
                            Date date = new Date(System.currentTimeMillis());
                            Log.i(TAG, "OnCompleted: "+date);
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                }
            });
            rfBleKey.setOnBleDevListChangeListener(new BleService.OnBleDevListChangeListener() {
                @Override
                public void onNewBleDev(BleDevContext bleDevContext) {
                    //发现新设备
                }
                @Override
                public void onUpdateBleDev(BleDevContext bleDevContext) {
                    //设备更新
                }
            });
//            rfBleKey.setOnCompletedListener(new OnCompletedListener() {
//                @Override
//                public void OnCompleted(byte[] bytes, int i) {
//                    final int result = i;
//                    switch (result) {
//                        case 0://成功
//                            Date date = new Date(System.currentTimeMillis());
//                            Log.i(TAG, "OnCompleted: "+date);
//                            break;
//                        case 1:
//                            break;
//                        case 2:
//                            break;
//                        case 3:
//                            break;
//                    }
//                }
//            });
        }

        @Override
        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //开启蓝牙服务
        Intent bindIntent = new Intent(getApplicationContext(), BleService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        //获取 SensorManager 负责管理传感器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        shakeListener = new ShakeListener();
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_NORMAL);
        Log.w(TAG, "in onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "in onStartCommand");
        //从服务器获取数据到本地数据库
        TaskComponent.getSubstationList(HttpService.this,0);
        //获取任务列表
        TaskComponent.getCommonTaskList(HttpService.this,11);
        //注册广播接收器(FullInspectActivity-->HttpService)
        FullActivityReceiver receiver=new HttpService.FullActivityReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.whut.smartinspection.activity.FullInspectActivity");
        HttpService.this.registerReceiver(receiver,filter);
        return START_STICKY;
    }
    /**
     * 获取广播数据
     *接收来自FullInspectionActivity的广播
     * @author jiqinlin
     *
     */
    public class FullActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {//广播接收
            Bundle bundle=intent.getExtras();
            String count=bundle.getString("flag");
            if("1".equals(count)){ //将本地数据库的巡视记录提交到服务器
                //查询一个设备的巡视项目
                WholePatrolCardDao wholePatrolCardDao = SApplication.getInstance().getDaoSession().getWholePatrolCardDao();
                QueryBuilder<WholePatrolCard> qbWholePatrolCard = wholePatrolCardDao.queryBuilder();
                List<WholePatrolCard> wholePatrolCards  = qbWholePatrolCard.list();
                RecordDao recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
                for(WholePatrolCard wholePatrolCard : wholePatrolCards){
                    Long wholeID = wholePatrolCard.getId();
                    QueryBuilder<Record> qbRecord = recordDao.queryBuilder();
                    List<Record> records = qbRecord.where(RecordDao.Properties.WholeID.eq(wholeID)).list();
                    wholePatrolCard.setRecords(records);
                    String temp = wholePatrolCard.toString();
                    TaskComponent.commitDetialTask(HttpService.this,temp);
                }
            }
//            if("1".equals(count)) { //将本地数据库的巡视记录提交到服务器
//                //查询一个设备的巡视项目
//                    PerPatrolCardDao perPatrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
//                    QueryBuilder<PerPatrolCard> qbPer = perPatrolCardDao.queryBuilder();
//                    List<PerPatrolCard> lll = qbPer.list();
//                    List<PerPatrolCard> perPatrolCardList = qbPer.where(PerPatrolCardDao.Properties.Flag.eq(false)).list();
//                    RecordDao recordDao = SApplication.getInstance().getDaoSession().getRecordDao();
//                    for(PerPatrolCard temp : perPatrolCardList){
//                        Long id = temp.getId();
//                        QueryBuilder<Record> qbRecord = recordDao.queryBuilder();
//                        List<Record> records = null;
//                        if(id!=null) {
//                            records = qbRecord.list();
//                            records = qbRecord.where(RecordDao.Properties.Fid.eq(id)).list();
//                        }
//                        temp.setRecords(records);
//                        //按设备来提交（一个设备提交一次)
//                        String resultPerPatrolCard = temp.toString();
//                        TaskComponent.commitDetialTask(HttpService.this,resultPerPatrolCard);
//
//                        temp.setFlag(true);//标注已提交过
//                        perPatrolCardDao.insertOrReplace(temp);
//                    }
//            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetialSuccess(Object obj, EMsgType type, int flag,String id) {
        if(flag == 1) {//获取巡视任务对应的详细内容
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            PatrolTaskDetailDao patrolTaskDetailDao = SApplication.getInstance().getDaoSession().getPatrolTaskDetailDao();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();
                String patrolHeadPageId = jo.get("patrolHeadPageId").toString();//任务的id
                String patrolNameId = jo.get("patrolNameId").toString();//任务类型
                PatrolTaskDetail patrolTaskDetail = new PatrolTaskDetail(null,id,format(patrolHeadPageId), format(patrolNameId));
                patrolTaskDetailDao.insertOrReplace(patrolTaskDetail);
            }
        }
    }

    @Override
    public void onDetialFailure(Object obj, EMsgType type) {
        SystemUtils.showToast(HttpService.this,(String)obj);
    }

    //摇一摇监听器
    public class ShakeListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;
            if ((Math.abs(values[0]) > 22 || Math.abs(values[1]) > 22 || Math.abs(values[2]) > 22)) {
                //开锁
                ArrayList<BleDevContext> lst = rfBleKey.getDiscoveredDevices();
                for (BleDevContext dev:lst){
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(bytesToString(dev.mac))
                            .append(" (").append(dev.rssi).append(")");
                    data_list.add(stringBuffer.toString());
//                    adapter.add(stringBuffer.toString().toUpperCase());
                }
                try{
                    rfBleKey.openDoor(stringToBytes(data_list.get(0).substring(0,18)),5,"31313131313131313131313131313131");
                }catch (Exception e){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"权限或蓝牙没有打开",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                //摇动手机后，再伴随震动提示~~
                vibrator.vibrate(500);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
    public static byte[] stringToBytes(String outStr){
        if (outStr.length()!=18)
            return null;
        int len = outStr.length()/2;
        byte[] mac = new byte[len];
        for (int i = 0; i < len; i++){
            String s = outStr.substring(i*2,i*2+2);
            if (Integer.valueOf(s, 16)>0x7F) {
                mac[i] = (byte)(Integer.valueOf(s, 16) - 0xFF - 1);
            }else {
                mac[i] = Byte.valueOf(s, 16);
            }
        }
        return mac;
    }
    public static String bytesToString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString().toUpperCase();
    }
    @Override
    public void onTaskSuccess(Object obj, EMsgType type, int flag) {
        if(flag == 1){
            JsonObject jsonObject = new JsonParser().parse((String)obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
            subDao.deleteAll();

            for(int i = 0;i<jsonArray.size();i++) {
                QueryBuilder<Sub> qbD = subDao.queryBuilder();
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();
                String id = jo.get("id").toString();
                String name = jo.get("substationName").toString();
                Sub sub = new Sub(null,format(name),format(id));
                subDao.insertOrReplace(sub);
            }
        }
        if(flag == 2){
            JsonObject jsonObject = new JsonParser().parse((String)obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            DeviceTypeDao deviceTypeDao = SApplication.getInstance().getDaoSession().getDeviceTypeDao();
            deviceTypeDao.deleteAll();

            for(int i = 0;i<jsonArray.size();i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();

                String id = jo.get("id").toString();
                String name = jo.get("name").toString();
                String  no = jo.get("no").toString();

                DeviceType deviceType = new DeviceType(null,format(id),format(name),format(no));
                deviceTypeDao.insertOrReplace(deviceType);
            }
        }
        //获取间隔
        if(flag == 3) {
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            IntervalUnitDao intervalUnitDao = SApplication.getInstance().getDaoSession().getIntervalUnitDao();
            intervalUnitDao.deleteAll();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();

                String id = jo.get("id").toString();
                String name = jo.get("name").toString();
                IntervalUnit intervalUnit = new IntervalUnit(null,format(id),format(name));
                intervalUnitDao.insertOrReplace(intervalUnit);
            }
        }
        //设备名称
        if(flag == 5) {
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            DeviceDao deviceDao = SApplication.getInstance().getDaoSession().getDeviceDao();
            deviceDao.deleteAll();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();
                String idd = jo.get("id").toString();
                String name = jo.get("name").toString();
                String intervalUnitId = jo.get("intervalUnitId").toString();
                int deviceTypeId = Integer.parseInt(jo.get("deviceTypeId").toString());

                Device device = new Device(null,format(name),null,format(idd),format(intervalUnitId),deviceTypeId);
                deviceDao.insertOrReplace(device);
            }
        }
        //获取巡视作业卡
        if(flag == 6) {
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            PatrolContentDao patrolContentDao = SApplication.getInstance().getDaoSession().getPatrolContentDao();
            patrolContentDao.deleteAll();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();

                String id = jo.get("id").toString();
                String no = jo.get("no").toString();
                String part = jo.get("part").toString();
                String content = jo.get("content").toString();
                String isImportant = jo.get("isImportant").toString();
                String data = "nu";
                int deviceTypeId = Integer.parseInt(jo.get("deviceTypeId").toString());
                String patrolNameId = jo.get("patrolNameId").toString();
                String patrolContentTypeNo = jo.get("patrolContentTypeNo").toString(); //巡视项目数据类型编码用于指定填RecordPostVo中的哪一个value字段，详情见附件1
                String patrolContentName = jo.get("patrolContentTypeName").toString(); //巡视项目数据类型


                PatrolContent patrolContent = new PatrolContent(null,format(id),Integer.parseInt(no),part,content,
                        Short.parseShort(isImportant),data,format(patrolContentTypeNo),
                        format(patrolContentName),deviceTypeId,format(patrolNameId));
                patrolContentDao.insertOrReplace(patrolContent);
            }
        }
        if(flag == 7) {//获取任务列表
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            TaskItemDao taskItemDao = SApplication.getInstance().getDaoSession().getTaskItemDao();

            taskItemDao.deleteAll();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();
            }
        }
        if(flag == 8) {//获取全部巡视作业卡名称
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            PatrolWorkCardDao patrolWorkCardDao = SApplication.getInstance().getDaoSession().getPatrolWorkCardDao();
            patrolWorkCardDao.deleteAll();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();

                String id = jo.get("id").toString();
                String typeName = jo.get("typeName").toString();
                String name = jo.get("name").toString();
                String typeId = jo.get("typeId").toString();

                PatrolWorkCard patrolWorkCard = new PatrolWorkCard(null,format(id),format(name),format(typeName),typeId,null);
                patrolWorkCardDao.insertOrReplace(patrolWorkCard);
            }
        }
        if(flag == 9) {//获取通用任务列表

            SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
            JsonObject jsonObject = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            TaskItemDao taskItemDao = SApplication.getInstance().getDaoSession().getTaskItemDao();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();
                String id = jo.get("id").toString();//任务的id
                String common = jo.get("comment").toString();//备注
                String taskType = jo.get("taskType").toString();//任务类型
//                Date startDate = null,endDate = null ;

//                try {
//                    startDate = sdr.parse(jo.get("startDate").toString());
//                    startDate = sdr.parse(jo.get("endDate").toString());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                String leader1 = jo.get("leader").toString();
                JsonParser jsonParser = new JsonParser();
                JsonElement jLeader = jsonParser.parse(leader1);
                JsonObject joLeader = jLeader.getAsJsonObject();
                String leader = joLeader.get("username").toString();

                TaskItem taskItem = new TaskItem(0L+i, format(id), format(leader),null,null,1,
                        format(taskType), null, 1,format(common));
                taskItemDao.insertOrReplace(taskItem);

                if("0".equals(format(taskType))){
                    PatrolTaskDetailDao patrolTaskDetailDao = SApplication.getInstance().getDaoSession().getPatrolTaskDetailDao();
                    patrolTaskDetailDao.deleteAll();
                    TaskComponent.getDetialPatrolTask(HttpService.this,format(id),format(id));//根据任务ID查询任务详情
                }
            }
            //发消息给HomePageActivity
            Intent intent = new Intent();
            intent.putExtra("flag","1");
            intent.setAction("com.whut.smartinspection.services.HttpService");
            sendBroadcast(intent);
        }
        if(flag == 11){//提交巡视项目回调
            SystemUtils.showToast(HttpService.this,"提交成功");
            if(obj!=null) {
//                JsonObject jsonObject = new JsonParser().parse(obj).getAsJsonObject();
//                String s = jsonObject.toString();
                Log.i(TAG, "onTaskSuccess: " + "ddd");
            }
        }
    }

    private String format(String str){
        if(str.length()>2)
            return str.substring(1,str.length()-1);
        return str;
    }
    @Override
    public void onTaskFailure(Object obj, EMsgType type) {
        Toast.makeText(HttpService.this,"请求服务器出错!",Toast.LENGTH_LONG);
    }
}
