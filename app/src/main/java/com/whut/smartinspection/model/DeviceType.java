package com.whut.smartinspection.model;

//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;


/**
 * Created by xms on 2017/10/26.
 * 设备类型
 */
@Entity
public class DeviceType {
    @Id
    @Property(nameInDb = "id")
    private Long id;
    //类型名称
    private String name;
    private String no;

    @Generated(hash = 940470995)
    public DeviceType(Long id, String name, String no) {
        this.id = id;
        this.name = name;
        this.no = no;
    }

    @Generated(hash = 1621062569)
    public DeviceType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
//类型编码

    public String getName() {
        return this.name;
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
