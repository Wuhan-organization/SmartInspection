package com.whut.smartinspection.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PatrolWorkCardDao;
import com.whut.greendao.gen.SubDao;
import com.whut.greendao.gen.TaskItemDao;
import com.whut.smartinspection.activity.MyTaskActivity;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.PatrolWorkCard;
import com.whut.smartinspection.model.Sub;
import com.whut.smartinspection.model.TaskItem;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Fortuner on 2017/12/5.
 */

public class HttpService extends Service implements ITaskHandlerListener{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "in onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "in onStartCommand");

        //从服务器获取数据到本地数据库
        TaskComponent.getSubstationList(HttpService.this,0);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "in onDestroy");
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
                String patrolContentName = ""; //巡视项目数据类型


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

                String id = jo.get("id").toString();
                String content = jo.get("content").toString();
                String typee = jo.get("type").toString();
                String worker = jo.get("worker").toString();
                String date = jo.get("date").toString();
                String status = jo.get("status").toString();
                String substationId = jo.get("substationName").toString();
//                String patrolTypeId = jo.get("patrolTypeId").toString();

                TaskItem taskItem = new TaskItem(null,format(id),format(content),format(typee),format(worker),
                        format(date),Integer.parseInt(status),format(substationId),0);
                taskItemDao.insertOrReplace(taskItem);
            }
        }
        if(flag == 8) {//获取全部巡视作业卡
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
