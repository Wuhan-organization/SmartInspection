package com.whut.smartinspection.model;

import java.io.Serializable;

/**
 * Created by Fortuner on 2017/11/20.
 */

public class VoltageClass implements Serializable {

    public VoltageClass(int id, String name, int idDelete) {
        this.id = id;
        this.name = name;
        this.idDelete = idDelete;
    }

    private int id;

    private String name;

    private int  idDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdDelete() {
        return idDelete;
    }

    public void setIdDelete(int idDelete) {
        this.idDelete = idDelete;
    }
}
