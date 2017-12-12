package com.whut.smartinspection.model;

import java.util.List;

/**
 * Created by Fortuner on 2017/12/12.
 */

public class PatrolRecordsPostVo {

    private List<Record> records;
    private String patrolHeadPageId; //首页Id

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

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getPatrolHeadPageId() {
        return patrolHeadPageId;
    }

    public void setPatrolHeadPageId(String patrolHeadPageId) {
        this.patrolHeadPageId = patrolHeadPageId;
    }

}
