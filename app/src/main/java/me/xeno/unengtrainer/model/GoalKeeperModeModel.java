package me.xeno.unengtrainer.model;

import java.util.ArrayList;

import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2018/3/4.
 * 门将训练模式：固定角度以后，循环或随机循环几组左右轮转速，以不可预测的方向射向球门
 */

public class GoalKeeperModeModel {

    private ArrayList<String> mSpeedSetList = new ArrayList<>();//装载所有转速组

    public int[] getSpeedSet(int index) {

        if (index < mSpeedSetList.size() && mSpeedSetList.get(index) != null) {
            int left = 0;
            int right = 0;
            try {
                String[] set = mSpeedSetList.get(index).split(",");
                left = Integer.valueOf(set[0]);
                right = Integer.valueOf(set[1]);
            } catch ( Exception e) {
                e.printStackTrace();
            }
            return new int[]{left, right};
        } else {
            Logger.warning("getSpeedSet(): index error.");
            return new int[]{0, 0};
        }
    }

    public int addSpeedSet(int left, int right) {
        StringBuilder sb = new StringBuilder();
        sb.append(left).append(",").append("right");
        mSpeedSetList.add(sb.toString());
        return mSpeedSetList.size() - 1;
    }

}
