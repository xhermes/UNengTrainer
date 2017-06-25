package me.xeno.unengtrainer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.xeno.unengtrainer.R;

/**
 * Created by xeno on 2017/5/22.
 */

public class FavouriteHolder extends RecyclerView.ViewHolder  {

    private View rootView;
    private TextView nameView;
    private TextView modifyTimeView;

    private TextView swingAngleView;
    private TextView elevationAngleView;
    private TextView leftSpeedView;
    private TextView rightSpeedView;

    public FavouriteHolder(View itemView) {
        super(itemView);
        rootView =  itemView.findViewById(R.id.root);
        nameView = (TextView) itemView.findViewById(R.id.name);
        modifyTimeView = (TextView) itemView.findViewById(R.id.time);
        swingAngleView = (TextView) itemView.findViewById(R.id.swing_angle);
        elevationAngleView = (TextView) itemView.findViewById(R.id.elevation_angle);
        leftSpeedView = (TextView) itemView.findViewById(R.id.left_speed);
        rightSpeedView = (TextView) itemView.findViewById(R.id.right_speed);
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public TextView getModifyTimeView() {
        return modifyTimeView;
    }

    public void setModifyTimeView(TextView modifyTimeView) {
        this.modifyTimeView = modifyTimeView;
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
