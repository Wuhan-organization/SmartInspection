package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Fortuner on 2017/11/24.
 */
@Entity
public class Sub  {
    @Id
    private String id;

    private String name;

    @Generated(hash = 1040193671)
    public Sub(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1375159814)
    public Sub() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
