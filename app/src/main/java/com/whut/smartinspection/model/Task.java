package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Map;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Fortuner on 2017/12/1.
 */
@Entity
public class Task {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;

    private String subId;

    private String subName;

    private String deviceId;

    private String deviceName;

    private String deviceTypeId;

    private String getDeviceName;

    private String intervalUnitId;

    private String intervalUnitName;

    private String patrolContentId;

    private String patrolContentName;

//    private Map<String,String> map ;

    private String problem;

    private String bug;

    private String danger;

    @Generated(hash = 1762107)
    public Task(Long id, String subId, String subName, String deviceId,
            String deviceName, String deviceTypeId, String getDeviceName,
            String intervalUnitId, String intervalUnitName, String patrolContentId,
            String patrolContentName, String problem, String bug, String danger) {
        this.id = id;
        this.subId = subId;
        this.subName = subName;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceTypeId = deviceTypeId;
        this.getDeviceName = getDeviceName;
        this.intervalUnitId = intervalUnitId;
        this.intervalUnitName = intervalUnitName;
        this.patrolContentId = patrolContentId;
        this.patrolContentName = patrolContentName;
        this.problem = problem;
        this.bug = bug;
        this.danger = danger;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getGetDeviceName() {
        return getDeviceName;
    }

    public void setGetDeviceName(String getDeviceName) {
        this.getDeviceName = getDeviceName;
    }

    public String getIntervalUnitId() {
        return intervalUnitId;
    }

    public void setIntervalUnitId(String intervalUnitId) {
        this.intervalUnitId = intervalUnitId;
    }

    public String getIntervalUnitName() {
        return intervalUnitName;
    }

    public void setIntervalUnitName(String intervalUnitName) {
        this.intervalUnitName = intervalUnitName;
    }

    public String getPatrolContentId() {
        return patrolContentId;
    }

    public void setPatrolContentId(String patrolContentId) {
        this.patrolContentId = patrolContentId;
    }

    public String getPatrolContentName() {
        return patrolContentName;
    }

    public void setPatrolContentName(String patrolContentName) {
        this.patrolContentName = patrolContentName;
    }

//    public Map<String, String> getMap() {
//        return map;
//    }
//
//    public void setMap(Map<String, String> map) {
//        this.map = map;
//    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"subId\":\"")
                .append(subId).append('\"');
        sb.append(",\"subName\":\"")
                .append(subName).append('\"');
        sb.append(",\"deviceId\":\"")
                .append(deviceId).append('\"');
        sb.append(",\"deviceName\":\"")
                .append(deviceName).append('\"');
        sb.append(",\"deviceTypeId\":\"")
                .append(deviceTypeId).append('\"');
        sb.append(",\"getDeviceName\":\"")
                .append(getDeviceName).append('\"');
        sb.append(",\"intervalUnitId\":\"")
                .append(intervalUnitId).append('\"');
        sb.append(",\"intervalUnitName\":\"")
                .append(intervalUnitName).append('\"');
        sb.append(",\"patrolContentId\":\"")
                .append(patrolContentId).append('\"');
        sb.append(",\"patrolContentName\":\"")
                .append(patrolContentName).append('\"');
        sb.append(",\"problem\":\"")
                .append(problem).append('\"');
        sb.append(",\"bug\":\"")
                .append(bug).append('\"');
        sb.append(",\"danger\":\"")
                .append(danger).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getBug() {
        return bug;
    }

    public void setBug(String bug) {
        this.bug = bug;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }
}
