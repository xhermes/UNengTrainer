package me.xeno.unengtrainer.model.entity;

/**
 * Created by Administrator on 2017/6/24.
 */

public class MakeZeroCompletedWrapper {

    private GetStatusWrapper.AxisStatus[] axisStatuses;//第一轴 & 第二轴

    public GetStatusWrapper.AxisStatus[] getAxisStatuses() {
        return axisStatuses;
    }

    public void setAxisStatuses(GetStatusWrapper.AxisStatus[] axisStatuses) {
        this.axisStatuses = axisStatuses;
    }
}
