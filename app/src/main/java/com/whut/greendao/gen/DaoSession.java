package com.whut.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.whut.smartinspection.model.Device;
import com.whut.smartinspection.model.DeviceType;
import com.whut.smartinspection.model.IntervalUnit;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.Person;
import com.whut.smartinspection.model.Sub;
import com.whut.smartinspection.model.Task;
import com.whut.smartinspection.model.TaskItem;
import com.whut.smartinspection.model.PatrolWorkCard;

import com.whut.greendao.gen.DeviceDao;
import com.whut.greendao.gen.DeviceTypeDao;
import com.whut.greendao.gen.IntervalUnitDao;
import com.whut.greendao.gen.PatrolContentDao;
import com.whut.greendao.gen.PersonDao;
import com.whut.greendao.gen.SubDao;
import com.whut.greendao.gen.TaskDao;
import com.whut.greendao.gen.TaskItemDao;
import com.whut.greendao.gen.PatrolWorkCardDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig deviceDaoConfig;
    private final DaoConfig deviceTypeDaoConfig;
    private final DaoConfig intervalUnitDaoConfig;
    private final DaoConfig patrolContentDaoConfig;
    private final DaoConfig personDaoConfig;
    private final DaoConfig subDaoConfig;
    private final DaoConfig taskDaoConfig;
    private final DaoConfig taskItemDaoConfig;
    private final DaoConfig patrolWorkCardDaoConfig;

    private final DeviceDao deviceDao;
    private final DeviceTypeDao deviceTypeDao;
    private final IntervalUnitDao intervalUnitDao;
    private final PatrolContentDao patrolContentDao;
    private final PersonDao personDao;
    private final SubDao subDao;
    private final TaskDao taskDao;
    private final TaskItemDao taskItemDao;
    private final PatrolWorkCardDao patrolWorkCardDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        deviceDaoConfig = daoConfigMap.get(DeviceDao.class).clone();
        deviceDaoConfig.initIdentityScope(type);

        deviceTypeDaoConfig = daoConfigMap.get(DeviceTypeDao.class).clone();
        deviceTypeDaoConfig.initIdentityScope(type);

        intervalUnitDaoConfig = daoConfigMap.get(IntervalUnitDao.class).clone();
        intervalUnitDaoConfig.initIdentityScope(type);

        patrolContentDaoConfig = daoConfigMap.get(PatrolContentDao.class).clone();
        patrolContentDaoConfig.initIdentityScope(type);

        personDaoConfig = daoConfigMap.get(PersonDao.class).clone();
        personDaoConfig.initIdentityScope(type);

        subDaoConfig = daoConfigMap.get(SubDao.class).clone();
        subDaoConfig.initIdentityScope(type);

        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        taskDaoConfig.initIdentityScope(type);

        taskItemDaoConfig = daoConfigMap.get(TaskItemDao.class).clone();
        taskItemDaoConfig.initIdentityScope(type);

        patrolWorkCardDaoConfig = daoConfigMap.get(PatrolWorkCardDao.class).clone();
        patrolWorkCardDaoConfig.initIdentityScope(type);

        deviceDao = new DeviceDao(deviceDaoConfig, this);
        deviceTypeDao = new DeviceTypeDao(deviceTypeDaoConfig, this);
        intervalUnitDao = new IntervalUnitDao(intervalUnitDaoConfig, this);
        patrolContentDao = new PatrolContentDao(patrolContentDaoConfig, this);
        personDao = new PersonDao(personDaoConfig, this);
        subDao = new SubDao(subDaoConfig, this);
        taskDao = new TaskDao(taskDaoConfig, this);
        taskItemDao = new TaskItemDao(taskItemDaoConfig, this);
        patrolWorkCardDao = new PatrolWorkCardDao(patrolWorkCardDaoConfig, this);

        registerDao(Device.class, deviceDao);
        registerDao(DeviceType.class, deviceTypeDao);
        registerDao(IntervalUnit.class, intervalUnitDao);
        registerDao(PatrolContent.class, patrolContentDao);
        registerDao(Person.class, personDao);
        registerDao(Sub.class, subDao);
        registerDao(Task.class, taskDao);
        registerDao(TaskItem.class, taskItemDao);
        registerDao(PatrolWorkCard.class, patrolWorkCardDao);
    }
    
    public void clear() {
        deviceDaoConfig.clearIdentityScope();
        deviceTypeDaoConfig.clearIdentityScope();
        intervalUnitDaoConfig.clearIdentityScope();
        patrolContentDaoConfig.clearIdentityScope();
        personDaoConfig.clearIdentityScope();
        subDaoConfig.clearIdentityScope();
        taskDaoConfig.clearIdentityScope();
        taskItemDaoConfig.clearIdentityScope();
        patrolWorkCardDaoConfig.clearIdentityScope();
    }

    public DeviceDao getDeviceDao() {
        return deviceDao;
    }

    public DeviceTypeDao getDeviceTypeDao() {
        return deviceTypeDao;
    }

    public IntervalUnitDao getIntervalUnitDao() {
        return intervalUnitDao;
    }

    public PatrolContentDao getPatrolContentDao() {
        return patrolContentDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public SubDao getSubDao() {
        return subDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public TaskItemDao getTaskItemDao() {
        return taskItemDao;
    }

    public PatrolWorkCardDao getPatrolWorkCardDao() {
        return patrolWorkCardDao;
    }

}
