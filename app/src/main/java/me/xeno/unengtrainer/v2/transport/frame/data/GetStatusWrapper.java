package me.xeno.unengtrainer.v2.transport.frame.data;

/**
 * Created by Administrator on 2017/6/24.
 */

public class GetStatusWrapper {

    private AxisStatus[] axisStatuses;//第一轴 & 第二轴
    private boolean isMakeZeroCompleted;

    public boolean isMakeZeroCompleted() {
        return isMakeZeroCompleted;
    }

    public void setMakeZeroCompleted(boolean makeZeroCompleted) {
        isMakeZeroCompleted = makeZeroCompleted;
    }

    public AxisStatus[] getAxisStatuses() {
        return axisStatuses;
    }

    public void setAxisStatuses(AxisStatus[] axisStatuses) {
        this.axisStatuses = axisStatuses;
    }

    public static class AxisStatus {
        private int running;//运行状态
        private int abruptStopping;//急停中
        private int alerting;//报警中
        private int positiveSpacing;//正限位
        private int negativeSpacing;//负限位

        public int getRunning() {
            return running;
        }

        public void setRunning(int running) {
            this.running = running;
        }

        public int getAbruptStopping() {
            return abruptStopping;
        }

        public void setAbruptStopping(int abruptStopping) {
            this.abruptStopping = abruptStopping;
        }

        public int getAlerting() {
            return alerting;
        }

        public void setAlerting(int alerting) {
            this.alerting = alerting;
        }

        public int getPositiveSpacing() {
            return positiveSpacing;
        }

        public void setPositiveSpacing(int positiveSpacing) {
            this.positiveSpacing = positiveSpacing;
        }

        public int getNegativeSpacing() {
            return negativeSpacing;
        }

        public void setNegativeSpacing(int negativeSpacing) {
            this.negativeSpacing = negativeSpacing;
        }


    }
}
