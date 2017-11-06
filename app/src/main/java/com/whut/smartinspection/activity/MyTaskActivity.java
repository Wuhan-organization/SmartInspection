package com.whut.smartinspection.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whut.smartinspection.R;
import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.component.http.WeatherComponent;
import com.whut.smartinspection.model.ResultObject;
import com.whut.smartinspection.utils.SystemUtils;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的任务页面
 * Created by xiongbin on 2017/11/3.
 */
public class MyTaskActivity extends SwipeBackActivity implements IHandlerListener {

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

        WeatherComponent.getWeathers(this, "南昌");
    }

    @OnClick({R.id.tv_my_task_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_my_task_back:
                finish();
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
        str = str[2].split(":");
        return str[1].replace("℃","");
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
                        Glide.with(this).load(current.getDayPictureUrl()).into(ivMyTaskWeatherPicture);
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
