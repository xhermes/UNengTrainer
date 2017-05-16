package me.xeno.unengtrainer.view.holder;

/**
 * Created by xeno on 2017/5/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.xeno.unengtrainer.R;

/**
 * Created by xeno on 2017/4/11.
 */

public class ShortcutHolder extends RecyclerView.ViewHolder  {

    private TextView nameView;
    private TextView swingAngleView;
    private TextView elevationAngleView;
    private TextView leftSpeedView;
    private TextView rightSpeedView;

    public ShortcutHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.name);
        swingAngleView = (TextView) itemView.findViewById(R.id.swing_angle);
        elevationAngleView = (TextView) itemView.findViewById(R.id.elevation_angle);
        leftSpeedView = (TextView) itemView.findViewById(R.id.left_speed);
        rightSpeedView = (TextView) itemView.findViewById(R.id.right_speed);
    }

    public TextView getNameView() {
        return nameView;
    }

    public void setNameView(TextView nameView) {
        this.nameView = nameView;
    }

    public TextView getSwingAngleView() {
        return swingAngleView;
    }

    public void setSwingAngleView(TextView swingAngleView) {
        this.swingAngleView = swingAngleView;
    }

    public TextView getElevationAngleView() {
        return elevationAngleView;
    }

    public void setElevationAngleView(TextView elevationAngleView) {
        this.elevationAngleView = elevationAngleView;
    }

    public TextView getLeftSpeedView() {
        return leftSpeedView;
    }

    public void setLeftSpeedView(TextView leftSpeedView) {
        this.leftSpeedView = leftSpeedView;
    }

    public TextView getRightSpeedView() {
        return rightSpeedView;
    }

    public void setRightSpeedView(TextView rightSpeedView) {
        this.rightSpeedView = rightSpeedView;
    }
}
