package me.xeno.unengtrainer.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/25.
 */

@Entity
public class FavouriteRecord {
    @Id(autoincrement = true)
    private Long id = null;

    private String name;
    private String createTime;
    private String modifyTime;

    private double swingAngle;
    private double elevationAngle;
    private int leftMotorSpeed;
    private int rightMotorSpeed;
    @Generated(hash = 1644622795)
    public FavouriteRecord(Long id, String name, String createTime,
            String modifyTime, double swingAngle, double elevationAngle,
            int leftMotorSpeed, int rightMotorSpeed) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.swingAngle = swingAngle;
        this.elevationAngle = elevationAngle;
        this.leftMotorSpeed = leftMotorSpeed;
        this.rightMotorSpeed = rightMotorSpeed;
    }
    @Generated(hash = 902397497)
    public FavouriteRecord() {
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
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getModifyTime() {
        return this.modifyTime;
    }
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    public double getSwingAngle() {
        return this.swingAngle;
    }
    public void setSwingAngle(double swingAngle) {
        this.swingAngle = swingAngle;
    }
    public double getElevationAngle() {
        return this.elevationAngle;
    }
    public void setElevationAngle(double elevationAngle) {
        this.elevationAngle = elevationAngle;
    }
    public int getLeftMotorSpeed() {
        return this.leftMotorSpeed;
    }
    public void setLeftMotorSpeed(int leftMotorSpeed) {
        this.leftMotorSpeed = leftMotorSpeed;
    }
    public int getRightMotorSpeed() {
        return this.rightMotorSpeed;
    }
    public void setRightMotorSpeed(int rightMotorSpeed) {
        this.rightMotorSpeed = rightMotorSpeed;
    }
  

}
