package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Fortuner on 2017/12/24.
 * 巡视任务 的详细任务细节
 */
@Entity
public class PatrolTaskDetail {
    //实体id
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;
    //任务id
    private String taskId;
    //巡视首页ID
    private String patrolHeadPageId;
    //巡视作业卡ID
    private String patrolNameId;

    @Generated(hash = 81408514)
    public PatrolTaskDetail(Long id, String taskId, String patrolHeadPageId,
            String patrolNameId) {
        this.id = id;
        this.taskId = taskId;
        this.patrolHeadPageId = patrolHeadPageId;
        this.patrolNameId = patrolNameId;
    }

    @Generated(hash = 621528201)
    public PatrolTaskDetail() {
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

    public String getPatrolNameId() {
        return patrolNameId;
    }

    public void setPatrolNameId(String patrolNameId) {
        this.patrolNameId = patrolNameId;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
