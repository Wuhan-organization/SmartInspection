package com.whut.smartinspection.component.http;

import com.whut.smartinspection.component.handler.EMsgType;
import com.whut.smartinspection.component.handler.IHandlerListener;
import com.whut.smartinspection.model.ResultObject;
import com.whut.smartinspection.parser.WeatherParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 天气相关
 * Created by xiongbin on 2017/11/3.
 */
public class WeatherComponent extends BaseHttpComponent {

    public static final String AK = "O22lDs7vDhBvHw5uQCfHM1GO0IelkdNg";

    public static void getWeathers(final IHandlerListener listener, String location) {

        String url = "http://api.map.baidu.com/telematics/v3/weather";
        OkHttpUtils.get()
                .url(url)
                .addParams("location", location)
                .addParams("output", "json")
                .addParams("ak", AK)
                .addParams("mcode", "0B:3F:0C:F4:92:85:47:21:94:16:9F:9C:37:13:FE:88:37:17:A3:A2;com.whut.smartinspection")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String message = "network error";
                if (e != null) {
                    message = e.getMessage();
                }
                listener.onFailure(message, EMsgType.GET_WEATHER_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultObject ro = WeatherParser.parseWeather(response);
                if (ro.getResults() != null) {
                    listener.onSuccess(ro.getResults(), EMsgType.GET_WEATHER_SUCCESS);
                } else {
                    listener.onFailure(ro.getStatus(), EMsgType.GET_WEATHER_FAILURE);
                }

            }
        });
    }


}
