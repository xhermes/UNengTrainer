package me.xeno.unengtrainer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.model.BleDevice;
import me.xeno.unengtrainer.view.holder.BluetoothHolder;

/**
 * Created by xeno on 2017/5/22.
 */

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothHolder>  {

    private List<BleDevice> dataList = new ArrayList<>();

    public void setDataList(List<BleDevice> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataToList(BleDevice device) {
        dataList.add(device);
        notifyDataSetChanged();
    }

    @Override
    public BluetoothHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_bluetooth, null);
        BluetoothHolder holder = new BluetoothHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BluetoothHolder holder, int position) {
        BleDevice device = dataList.get(position);

        holder.getNameView().setText(device.getName());
        holder.getAddressView().setText(device.getAddress());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
