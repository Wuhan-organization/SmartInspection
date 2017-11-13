package com.whut.smartinspection.model;

import java.io.Serializable;

/**
 * Created by Fortuner on 2017/11/12.
 */

public class Task implements Serializable{

    private String text ;

    private String stationName;

    private String number;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStationName() {
        return stationName;
    }

    public String getNumber() {
        return number;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
