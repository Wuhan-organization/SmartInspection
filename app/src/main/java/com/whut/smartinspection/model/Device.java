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

    @Generated(hash = 1996440647)
    public Device(Long id, String name, String no, String idd) {
        this.id = id;
        this.name = name;
        this.no = no;
        this.idd = idd;
    }

    @Generated(hash = 1469582394)
    public Device() {
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
