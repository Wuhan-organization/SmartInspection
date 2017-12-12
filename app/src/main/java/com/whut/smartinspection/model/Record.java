package com.whut.smartinspection.model;

import java.util.Date;

/**
 * Created by Fortuner on 2017/12/12.
 */

public class Record {
    private String id;
    private char valueChar;
    private float valueFloat;
    private String valueString;
    private long patrolRecordDate;
    private String deviceId;
    private String patrolContentId;


//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder("{");
//        sb.append("\"id\":\"")
//                .append(id).append('\"');
//        sb.append(",\"valueChar\":\"")
//                .append(valueChar).append("\"");
//        sb.append(",\"valueFloat\":\"")
//                .append(valueFloat).append("\"");
//        sb.append(",\"valueString\":\"")
//                .append(valueString).append('\"');
//        sb.append(",\"patrolRecordDate\":\"")
//                .append(patrolRecordDate).append("\"");
//        sb.append(",\"deviceId\":\"")
//                .append(deviceId).append('\"');
//        sb.append('}');
//        return sb.toString();
//    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"valueChar\":\"")
                .append(valueChar).append("\"");
        sb.append(",\"valueFloat\":\"")
                .append(valueFloat).append("\"");
        sb.append(",\"valueString\":\"")
                .append(valueString).append('\"');
        sb.append(",\"patrolRecordDate\":\"")
                .append(patrolRecordDate).append("\"");
        sb.append(",\"deviceId\":\"")
                .append(deviceId).append('\"');
        sb.append(",\"patrolContentId\":\"")
                .append(patrolContentId).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getPatrolContentId() {
        return patrolContentId;
    }

    public void setPatrolContentId(String patrolContentId) {
        this.patrolContentId = patrolContentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public char getValueChar() {
        return valueChar;
    }

    public void setValueChar(char valueChar) {
        this.valueChar = valueChar;
    }

    public float getValueFloat() {
        return valueFloat;
    }

    public void setValueFloat(float valueFloat) {
        this.valueFloat = valueFloat;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public long getPatrolRecordDate() {
        return patrolRecordDate;
    }

    public void setPatrolRecordDate(long patrolRecordDate) {
        this.patrolRecordDate = patrolRecordDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
