package com.whut.smartinspection.model;

//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xms on 2017/10/26.
 * 变电站信息
 */
public class Substation implements Serializable {

    public Substation(String id, String name, String deviceName, String deviceNo, String territorialCity, String operationCompany, String maintenanceTeam, String status, Date date, String location, String contract, String voltageClassName, short isDelete, VoltageClass voltageClass) {
        this.id = id;
        this.name = name;
        this.deviceName = deviceName;
        this.deviceNo = deviceNo;
        this.territorialCity = territorialCity;
        this.operationCompany = operationCompany;
        this.maintenanceTeam = maintenanceTeam;
        this.status = status;
        this.date = date;
        this.location = location;
        this.contract = contract;
        this.voltageClassName = voltageClassName;
        this.isDelete = isDelete;
        this.voltageClass = voltageClass;
    }

    private String id;
    //变电站名称
    private String name;
    //设备名称
    private String deviceName;
    //设备编码
    private String deviceNo;
    //所属地市
    private String territorialCity;
    //运维单位
    private String operationCompany;
    //维护班组
    private String maintenanceTeam;
    //设备状态
    private String status;
    //投运日期
    private Date date;
    //站址
    private String location;
    //联系方式
    private String contract;
    //电压等级名称
    private String voltageClassName;

    private short isDelete;
    //电压等级外键
    private VoltageClass voltageClass ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getTerritorialCity() {
        return territorialCity;
    }

    public void setTerritorialCity(String territorialCity) {
        this.territorialCity = territorialCity;
    }

    public String getOperationCompany() {
        return operationCompany;
    }

    public void setOperationCompany(String operationCompany) {
        this.operationCompany = operationCompany;
    }

    public String getMaintenanceTeam() {
        return maintenanceTeam;
    }

    public void setMaintenanceTeam(String maintenanceTeam) {
        this.maintenanceTeam = maintenanceTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getVoltageClassName() {
        return voltageClassName;
    }

    public void setVoltageClassName(String voltageClassName) {
        this.voltageClassName = voltageClassName;
    }

    public short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(short isDelete) {
        this.isDelete = isDelete;
    }

    public VoltageClass getVoltageClass() {
        return voltageClass;
    }

    public void setVoltageClass(VoltageClass voltageClass) {
        this.voltageClass = voltageClass;
    }
}
