package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by think on 2017/10/24.
 */
@Entity
public class Person {
    @Id
    private Long id;
    private String name;
    private int age;
    private String phoneNumber;
    private String password;
    private String sessionId;
    private boolean isInitTaskDetail ;//是否初始化变电站信息

    @Generated(hash = 1756947697)
    public Person(Long id, String name, int age, String phoneNumber,
            String password, String sessionId, boolean isInitTaskDetail) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.sessionId = sessionId;
        this.isInitTaskDetail = isInitTaskDetail;
    }

    @Generated(hash = 1024547259)
    public Person() {
    }

    public boolean isInitTaskDetail() {
        return isInitTaskDetail;
    }

    public void setInitTaskDetail(boolean initTaskDetail) {
        isInitTaskDetail = initTaskDetail;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public boolean getIsInitTaskDetail() {
        return this.isInitTaskDetail;
    }

    public void setIsInitTaskDetail(boolean isInitTaskDetail) {
        this.isInitTaskDetail = isInitTaskDetail;
    }
}
