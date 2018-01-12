package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xms on 2017/10/24.
 * 巡视内容
 */
@Entity
public class PatrolContent {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;

    private String idd;
    //项目条目
    private int no;
    //巡视部位
    private String part;
    //内容及要求
    private String content;
    //是否为关键步骤
    private short isImportant;
    //添加时间
    private String date;
    //数据类型
    private String patrolContentTypeNo;

    private String patrolContentName; //巡视项目数据类型

    private int deviceTypeId;

    private String patrolNameId;

    private String unit;

    @Generated(hash = 1410287866)
    public PatrolContent(Long id, String idd, int no, String part, String content,
            short isImportant, String date, String patrolContentTypeNo,
            String patrolContentName, int deviceTypeId, String patrolNameId,
            String unit) {
        this.id = id;
        this.idd = idd;
        this.no = no;
        this.part = part;
        this.content = content;
        this.isImportant = isImportant;
        this.date = date;
        this.patrolContentTypeNo = patrolContentTypeNo;
        this.patrolContentName = patrolContentName;
        this.deviceTypeId = deviceTypeId;
        this.patrolNameId = patrolNameId;
        this.unit = unit;
    }

    @Generated(hash = 1821211469)
    public PatrolContent() {
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPatrolContentTypeNo() {
        return patrolContentTypeNo;
    }

    public void setPatrolContentTypeNo(String patrolContentTypeNo) {
        this.patrolContentTypeNo = patrolContentTypeNo;
    }

    public String getPatrolContentName() {
        return patrolContentName;
    }

    public void setPatrolContentName(String patrolContentName) {
        this.patrolContentName = patrolContentName;
    }

    public int getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getPatrolNameId() {
        return patrolNameId;
    }

    public void setPatrolNameId(String patrolNameId) {
        this.patrolNameId = patrolNameId;
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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public short getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(short isImportant) {
        this.isImportant = isImportant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
