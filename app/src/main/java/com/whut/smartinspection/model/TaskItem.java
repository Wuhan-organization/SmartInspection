package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fortuner on 2017/12/5.
 * 任务实体
 */
@Entity
public class TaskItem  implements Serializable{
    final static long   serialVersionUID = 1;
    @Property(nameInDb = "id")
    @Id()
    private Long id;//前端自动生成Id
    private String idd; //任务id
    private String worker;//工作者 列表 负责人放在第一位
    private Date startDate;//任务开始日期
    private Date endDate;//任务结束日期
    private int status; //完成状态1完成，0代办--》改为表示是否初始化
    private String taskType;//任务类型
    private String taskTypeName;//任务类型名称 eg.全面巡视
    private int taskIcon;//任务类型图标
    private String common;//备注

    @Generated(hash = 874878458)
    public TaskItem(Long id, String idd, String worker, Date startDate,
            Date endDate, int status, String taskType, String taskTypeName,
            int taskIcon, String common) {
        this.id = id;
        this.idd = idd;
        this.worker = worker;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.taskType = taskType;
        this.taskTypeName = taskTypeName;
        this.taskIcon = taskIcon;
        this.common = common;
    }

    @Generated(hash = 910645620)
    public TaskItem() {
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }


//    private String substatioinName;//变电站名称
//    private String substationId;//变电站id
//    private String deviceTypeName;//设备类型名称
//    private String deviceTypeId;//设备类型ID
//    private String intervalUnitName;//间隔名称
//    private String intervalUnitId;//间隔ID
//    private String patrolStyleName;//巡视类型
//    private String patrolStyleId;//巡视类型ID
//    private String deviceName;//设备名称
//    private String deviceId;//设备ID
//    private String patrolContentName;//巡视项目名称
//    private String patrolContentId;//巡视项目ID

    public int getTaskIcon() {
        return taskIcon;
    }

    public void setTaskIcon(int taskIcon) {
        this.taskIcon = taskIcon;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
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

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
