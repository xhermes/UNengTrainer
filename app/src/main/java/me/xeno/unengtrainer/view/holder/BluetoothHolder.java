package me.xeno.unengtrainer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.xeno.unengtrainer.R;

/**
 * Created by xeno on 2017/5/22.
 */

public class BluetoothHolder extends RecyclerView.ViewHolder  {

    private TextView nameView;
    private TextView addressView;

    public BluetoothHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.name);
        addressView = (TextView) itemView.findViewById(R.id.address);
    }

    public TextView getNameView() {
        return nameView;
    }

    public void setNameView(TextView nameView) {
        this.nameView = nameView;
    }

    public TextView getAddressView() {
        return addressView;
    }

    public void setAddressView(TextView addressView) {
        this.addressView = addressView;
    }
}
