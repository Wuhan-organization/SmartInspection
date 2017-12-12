package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by xms on 2017/10/26.
 * 设备信息
 */
@Entity
public class Device {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;
    //设备名称
    private String name;
    //设备编号
    private String no;
    
    private String idd;

    private String intervalUnitId;

    private int deviceTypeId;

    @Generated(hash = 744949392)
    public Device(Long id, String name, String no, String idd,
            String intervalUnitId, int deviceTypeId) {
        this.id = id;
        this.name = name;
        this.no = no;
        this.idd = idd;
        this.intervalUnitId = intervalUnitId;
        this.deviceTypeId = deviceTypeId;
    }

    @Generated(hash = 1469582394)
    public Device() {
    }

    public String getIntervalUnitId() {
        return intervalUnitId;
    }

    public void setIntervalUnitId(String intervalUnitId) {
        this.intervalUnitId = intervalUnitId;
    }

    public int getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }


}
