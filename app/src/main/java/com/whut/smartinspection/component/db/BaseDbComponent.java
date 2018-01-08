package com.whut.smartinspection.component.db;

import com.whut.greendao.gen.PerPatrolCardDao;
import com.whut.greendao.gen.RecordDao;
import com.whut.greendao.gen.TaskItemDao;
import com.whut.greendao.gen.WholePatrolCardDao;
import com.whut.smartinspection.application.SApplication;
import com.whut.smartinspection.model.WholePatrolCard;
/***
 * @author xiongbin
 * @date 2016-3-31 下午2:35:18
 * @version 1.0
 * @description
 */
public class BaseDbComponent {
	
//	protected static FinalDb mFinalDb;
//
//	static {
//		Context context = SApplication.getInstance().getApplicationContext();
//		DaoConfig daoConfig = new DaoConfig();
//		daoConfig.setContext(context);
//		daoConfig.setDbName("city.db");
//		mFinalDb = FinalDb.create(daoConfig);
//
//	}
    public static TaskItemDao taskItemDao = SApplication.getInstance().getDaoSession().getTaskItemDao();
    public static WholePatrolCardDao wholePatrolCardDao = SApplication.getInstance().getDaoSession().getWholePatrolCardDao();
    public static PerPatrolCardDao perPatrolCardDao = SApplication.getInstance().getDaoSession().getPerPatrolCardDao();
    public static RecordDao recordDao = SApplication.getInstance().getDaoSession().getRecordDao();

    public static TaskItemDao getTaskItemDao(){
        return taskItemDao;
    }
    public static void deleteData(){
//        taskItemDao.deleteAll();
        wholePatrolCardDao.deleteAll();
        perPatrolCardDao.deleteAll();
        recordDao.deleteAll();
    }
}
