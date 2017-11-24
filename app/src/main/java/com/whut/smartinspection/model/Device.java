package com.whut.smartinspection.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by xms on 2017/10/26.
 * 设备信息
 */
@Entity
public class Device {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;
    //设备名称
    private String name;
    //设备编号
    private String no;

    @Generated(hash = 1450365040)
    public Device(Long id, String name, String no) {
        this.id = id;
        this.name = name;
        this.no = no;
    }
    @Keep
    public Device() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
//设备状态
//    @Column(length = 20, nullable = false)
//    private String status;
//    //运行编号
//    private String runningNo;
//    //相别
//    private String phase;
//    //是否删除
//    private short isDelete;
//    //间隔名称
//    @Column(nullable = false, length = 100)
//    private String intervalUnitName;
//    //设备类型名称
//    @Column(nullable = false, length = 100)
//    private String deviceTypeName;
//    //电压等级名称
//    @Column(nullable = false, length = 100)
//    private String voltageClassName;
//    //变电站名称
//    @Column(nullable = false, length = 100)
//    private String substationName;
//
//    //间隔
//    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
//    @JoinColumn(name = "interval_unit_id", referencedColumnName = "id")
//    private IntervalUnit intervalUnit;
//    //设备类型
//    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
//    @JoinColumn(name = "device_type_id", referencedColumnName = "id")
//    private DeviceType deviceType;
//
//    //电压等级
//    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
//    @JoinColumn(name = "voltage_class_id", referencedColumnName = "id")
//    private VoltageClass voltageClass;
//
//    //变电站
//    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
//    @JoinColumn(name = "substation_id", referencedColumnName = "id")
//    private Substation substation;
//
//    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<PatrolRecord> patrolRecords = new HashSet<>();

}
