package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.whut.greendao.gen.DaoSession;
import com.whut.greendao.gen.RecordDao;
import com.whut.greendao.gen.PerPatrolCardDao;

/**
 * Created by Fortuner on 2017/12/12.
 * 用于存储一个巡视作业卡的  (单个设备） 所有项目的提交信息
 */
@Entity
public class PerPatrolCard {
    //实体id
    @Property(nameInDb = "id")
    @Id()
    private Long id;

    @ToMany(referencedJoinProperty = "fid")
    private List<Record> records;//一个巡视卡下的所有巡视项目

    private String deviceId; //设备名称Id

    private boolean flag;//是否提交到服务器

    private Long fid;//外键

    private String patrolHeadPageId;//首页id

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2096211651)
    private transient PerPatrolCardDao myDao;

    @Generated(hash = 2083541088)
    public PerPatrolCard(Long id, String deviceId, boolean flag, Long fid,
            String patrolHeadPageId) {
        this.id = id;
        this.deviceId = deviceId;
        this.flag = flag;
        this.fid = fid;
        this.patrolHeadPageId = patrolHeadPageId;
    }

    @Generated(hash = 1716305057)
    public PerPatrolCard() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"records\":")
                .append(records);
        sb.append(",\"patrolHeadPageId\":\"")
                .append(patrolHeadPageId).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getPatrolHeadPageId() {
        return patrolHeadPageId;
    }

    public void setPatrolHeadPageId(String patrolHeadPageId) {
        this.patrolHeadPageId = patrolHeadPageId;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Keep
    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return this.flag;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 3024737)
    public synchronized void resetRecords() {
        records = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 903099411)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPerPatrolCardDao() : null;
    }

}
