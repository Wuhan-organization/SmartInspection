package com.whut.smartinspection.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whut.smartinspection.model.ResultObject;

/**
 * Created by xiongbin on 2017/11/3.
 */
public class WeatherParser {

    public static ResultObject parseWeather(String json) {
        ResultObject ro = new Gson().fromJson(json, new TypeToken<ResultObject>(){}.getType());
        return ro;
    }
}
