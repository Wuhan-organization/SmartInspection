package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whut.baidu.location.LocationService;
import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.SubDao;
import com.whut.smartinspection.R;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.handler.ITaskHandlerListener;
import com.whut.smartinspection.component.http.TaskComponent;
import com.whut.smartinspection.component.http.WeatherComponent;
import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.ResultObject;
import com.whut.smartinspection.model.Sub;
import com.whut.smartinspection.utils.SystemUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的任务页面
 * Created by xiongbin on 2017/11/3.
 */
public class MyTaskActivity extends SwipeBackActivity implements IHandlerListener,ITaskHandlerListener {

    @BindView(R.id.tv_my_task_temperature)
    TextView tvMyTaskTemperature;

    @BindView(R.id.tv_my_task_wind)
    TextView tvMyTaskWind;

    @BindView(R.id.tv_my_task_weather)
    TextView tvMyTaskWeather;

    @BindView(R.id.iv_my_task_weather_picture)
    ImageView ivMyTaskWeatherPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        ButterKnife.bind(this);

        MyLocationListener myListener = new MyLocationListener();
        LocationService.getInstance(this).registerListener(myListener);
        initData();
    }
    private void initData(){
        //从服务器获取变电站名称到本地数据库
        TaskComponent.getSubstationList(MyTaskActivity.this,0);
        //从服务器获取设备类型到本地数据库
//        TaskComponent.getDeviceStyleList(MyTaskActivity.this,2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationService.getInstance(this).startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationService.getInstance(this).stopLocation();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }
            String city = bdLocation.getCity();
            WeatherComponent.getWeathers(MyTaskActivity.this, city);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @OnClick({R.id.tv_my_task_back,R.id.waiting_task,R.id.completed_task,R.id.new_task})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            // 返回
            case R.id.tv_my_task_back:
                finish();
                break;
            case R.id.waiting_task:
                intent = new Intent(MyTaskActivity.this,WaitingTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.new_task:
                intent = new Intent(MyTaskActivity.this,NewTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.completed_task:

                intent = new Intent(MyTaskActivity.this,CompletedTaskActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /***
     * 获取当前温度
     * @param date
     * @return
     */
    private String getCurrentTemperature(String date) {
        if (date == null) {
            return null;
        }
        String[] str = date.split(" ");
        str = str[2].split(":|：");
        if (str != null && str.length > 1) {
            return str[1].replace(")","").replace("）","");
        } else {
            return "";
        }
    }

    @Override
    public void onSuccess(Object obj, EMsgType type) {
        switch (type) {
            // 获取天气成功
            case GET_WEATHER_SUCCESS:
                ResultObject.Result[] results = (ResultObject.Result[])obj;
                if (results != null && results.length > 0) {
                    ResultObject.Result result = results[0];
                    ResultObject.Weather[] weathers = result.getWeatherData();
                    if (weathers != null && weathers.length > 0) {
                        ResultObject.Weather current = weathers[0];
                        tvMyTaskWind.setText(current.getWind());
                        tvMyTaskWeather.setText(current.getWeather());
                        Glide.with(SApplication.getInstance()).load(current.getDayPictureUrl()).into(ivMyTaskWeatherPicture);
                        String currentTemperature = getCurrentTemperature(current.getDate());
                        if (currentTemperature != null) {
                            tvMyTaskTemperature.setText(currentTemperature);
                        }
                    }
                }
                break;

            default:
                break;

        }
    }

    @Override
    public void onTaskSuccess(Object obj, EMsgType type, final int flag) {
        if(flag == 1){
            JsonObject jsonObject = new JsonParser().parse((String)obj).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            SubDao subDao = SApplication.getInstance().getDaoSession().getSubDao();
            subDao.deleteAll();
            for(int i = 0;i<jsonArray.size();i++) {
                JsonElement idx = jsonArray.get(i);
                JsonObject jo = idx.getAsJsonObject();

                String id = jo.get("id").toString();
                String name = jo.get("name").toString();

                Sub sub = new Sub(null,name.substring(1,name.length()-1),id.substring(1,id.length()-1));
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

                DeviceType deviceType = new DeviceType(Long.parseLong(id),name.substring(1,name.length()-1),no.substring(1,no.length()-1));
                deviceTypeDao.insertOrReplace(deviceType);
            }
        }
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

                IntervalUnit intervalUnit = new IntervalUnit(null,id.substring(1,id.length()-1),name.substring(1,name.length()-1));
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

                String id = jo.get("id").toString();
                String name = jo.get("name").toString();

                Device device = new Device(null,name.substring(1,name.length()-1),id.substring(1,id.length()-1));
                deviceDao.insertOrReplace(device);
            }
        }
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
                String patrolContentType = "nu";

                PatrolContent patrolContent = new PatrolContent(null,id.substring(1,id.length()-1),Integer.parseInt(no),part,content,
                        Short.parseShort(isImportant),data,patrolContentType);
                patrolContentDao.insertOrReplace(patrolContent);
            }
        }

        }

    @Override
    public void onTaskFailure(Object obj, EMsgType type) {

    }

    @Override
    public void onFailure(Object obj, EMsgType type) {
        switch (type) {
            // 获取天气失败
            case GET_WEATHER_FAILURE:
                SystemUtils.showToast(this, obj.toString());
                break;

            default:
                break;
        }
    }

}
