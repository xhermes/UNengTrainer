package me.xeno.unengtrainer.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/25.
 */
@Entity
public class HistoryRecord {

    @Id
    private long id;

    private String createTime;

    private double swingAngle;
    private double elevationAngle;
    private int leftMotorSpeed;
    private int rightMotorSpeed;
    @Generated(hash = 658306288)
    public HistoryRecord(long id, String createTime, double swingAngle,
            double elevationAngle, int leftMotorSpeed, int rightMotorSpeed) {
        this.id = id;
        this.createTime = createTime;
        this.swingAngle = swingAngle;
        this.elevationAngle = elevationAngle;
        this.leftMotorSpeed = leftMotorSpeed;
        this.rightMotorSpeed = rightMotorSpeed;
    }
    @Generated(hash = 725453896)
    public HistoryRecord() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
