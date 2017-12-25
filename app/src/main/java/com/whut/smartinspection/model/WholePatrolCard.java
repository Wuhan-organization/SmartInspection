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

/**
 * Created by Fortuner on 2017/12/25.
 */
@Entity
public class WholePatrolCard {
    @Id(autoincrement = true)
    private Long id;

    private String patrolHeadPageId;

    @ToMany(referencedJoinProperty = "fid")
    private List<PerPatrolCard> perPatrolCardList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 297373125)
    private transient WholePatrolCardDao myDao;

    @Generated(hash = 786956462)
    public WholePatrolCard(Long id, String patrolHeadPageId) {
        this.id = id;
        this.patrolHeadPageId = patrolHeadPageId;
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
    public List<PerPatrolCard> getPerPatrolCardList() {
        return perPatrolCardList;
    }

    public void setPerPatrolCardList(List<PerPatrolCard> perPatrolCardList) {
        this.perPatrolCardList = perPatrolCardList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1358819915)
    public synchronized void resetPerPatrolCardList() {
        perPatrolCardList = null;
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
