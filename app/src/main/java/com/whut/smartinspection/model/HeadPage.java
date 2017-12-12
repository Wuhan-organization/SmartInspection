package com.whut.smartinspection.model;

/**
 * Created by Fortuner on 2017/12/12.
 */

public class HeadPage {

    private String editorName;//巡视人名称
    private String substationId;
    private String patrolNameId;

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getSubstationId() {
        return substationId;
    }

    public void setSubstationId(String substationId) {
        this.substationId = substationId;
    }

    public String getPatrolNameId() {
        return patrolNameId;
    }

    public void setPatrolNameId(String patrolNameId) {
        this.patrolNameId = patrolNameId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"editorName\":\"")
                .append(editorName).append('\"');
        sb.append(",\"substationId\":\"")
                .append(substationId).append('\"');
        sb.append(",\"patrolNameId\":\"")
                .append(patrolNameId).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
