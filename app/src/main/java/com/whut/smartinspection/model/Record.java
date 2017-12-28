package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Fortuner on 2017/12/12.
 * 巡视作业卡下面的一个巡视项目记录
 */

@Entity
public class Record {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;//greenDao自动生成的ID

    private String idd;//巡视项目ID
    private String valueChar;
    private float valueFloat;
    private String valueString;
    private long patrolRecordDate;
    private String deviceId;
    private String patrolContentId;

    private Long fid;//用于标识外键关系

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(idd).append('\"');
        sb.append(",\"valueChar\":\"")
                .append(valueChar).append('\"');
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

    @Generated(hash = 426103613)
    public Record(Long id, String idd, String valueChar, float valueFloat,
            String valueString, long patrolRecordDate, String deviceId,
            String patrolContentId, Long fid) {
        this.id = id;
        this.idd = idd;
        this.valueChar = valueChar;
        this.valueFloat = valueFloat;
        this.valueString = valueString;
        this.patrolRecordDate = patrolRecordDate;
        this.deviceId = deviceId;
        this.patrolContentId = patrolContentId;
        this.fid = fid;
    }

    @Generated(hash = 477726293)
    public Record() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public String getPatrolContentId() {
        return patrolContentId;
    }

    public void setPatrolContentId(String patrolContentId) {
        this.patrolContentId = patrolContentId;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getValueChar() {
        return valueChar;
    }

    public void setValueChar(String valueChar) {
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
