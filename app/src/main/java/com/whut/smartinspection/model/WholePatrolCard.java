package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.whut.greendao.gen.DaoSession;
import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.greendao.gen.RecordDao;

/**
 * Created by Fortuner on 2017/12/25.
 * 巡视作业卡记录实体
 */
@Entity
public class WholePatrolCard {
    //实体id
    @Id(autoincrement = true)
    private Long id;
    //首页id
    private String patrolHeadPageId;

    //巡视作业卡下的巡视项目记录
    @ToMany(referencedJoinProperty = "fid")
    private List<Record> records;

    private boolean flag ;//标记是否提交到服务器

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 297373125)
    private transient WholePatrolCardDao myDao;

    @Generated(hash = 1785942662)
    public WholePatrolCard(Long id, String patrolHeadPageId, boolean flag) {
        this.id = id;
        this.patrolHeadPageId = patrolHeadPageId;
        this.flag = flag;
    }

    @Generated(hash = 1369273149)
    public WholePatrolCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatrolHeadPageId() {
        return patrolHeadPageId;
    }

    public void setPatrolHeadPageId(String patrolHeadPageId) {
        this.patrolHeadPageId = patrolHeadPageId;
    }
    @Keep
    public List<Record> getRecords() {
        return records;
    }
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"patrolHeadPageId\":\"")
                .append(patrolHeadPageId).append('\"');
        sb.append(",\"records\":")
                .append(records);
        sb.append('}');
        return sb.toString();
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
    @Generated(hash = 780103344)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWholePatrolCardDao() : null;
    }
}
