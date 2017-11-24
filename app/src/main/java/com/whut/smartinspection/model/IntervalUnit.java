package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Fortuner on 2017/11/24.
 */
@Entity
public class IntervalUnit {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;

    private String idd;

    private String name;

    @Generated(hash = 443661963)
    public IntervalUnit(Long id, String idd, String name) {
        this.id = id;
        this.idd = idd;
        this.name = name;
    }

    @Generated(hash = 1901412763)
    public IntervalUnit() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
