package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Fortuner on 2017/12/5.
 */
@Entity
public class TaskItem {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;

    private String idd;
    private String content;
    private String type;
    private String worker;      //工作者
    private String date;
    private int status;       //完成状态1完成，0代办
    private String substationId;
    private int patrolTypeId;

    @Generated(hash = 960819216)
    public TaskItem(Long id, String idd, String content, String type, String worker,
            String date, int status, String substationId, int patrolTypeId) {
        this.id = id;
        this.idd = idd;
        this.content = content;
        this.type = type;
        this.worker = worker;
        this.date = date;
        this.status = status;
        this.substationId = substationId;
        this.patrolTypeId = patrolTypeId;
    }

    @Generated(hash = 910645620)
    public TaskItem() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubstationId() {
        return substationId;
    }

    public void setSubstationId(String substationId) {
        this.substationId = substationId;
    }

    public int getPatrolTypeId() {
        return patrolTypeId;
    }

    public void setPatrolTypeId(int patrolTypeId) {
        this.patrolTypeId = patrolTypeId;
    }
}
