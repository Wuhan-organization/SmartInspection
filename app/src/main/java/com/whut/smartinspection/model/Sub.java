package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Fortuner on 2017/11/24.
 * 变电站实体
 */
@Entity
public class Sub  {
    //实体id
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;
    //变电站名称
    private String name;
    //变电站id
    @Property(nameInDb = "idd")
    private String idd;



    @Generated(hash = 816112984)
    public Sub(Long id, String name, String idd) {
        this.id = id;
        this.name = name;
        this.idd = idd;
    }

    @Generated(hash = 1375159814)
    public Sub() {
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
}
