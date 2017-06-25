package me.xeno.unengtrainer.model.entity;

/**
 * Created by Administrator on 2017/6/24.
 */

public class EnableWrapper {

    public static final int ERR_STATUS_NORMAL = 0;
    public static final int ERR_STATUS_ALERT = 1;

    private int axis1Alert;
    private int axis2Alert;

    private boolean isSuccess;

    public int getAxis1Alert() {
        return axis1Alert;
    }

    public void setAxis1Alert(int axis1Alert) {
        this.axis1Alert = axis1Alert;
    }

    public int getAxis2Alert() {
        return axis2Alert;
    }

    public void setAxis2Alert(int axis2Alert) {
        this.axis2Alert = axis2Alert;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
