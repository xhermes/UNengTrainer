package me.xeno.unengtrainer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.view.holder.BluetoothHolder;

/**
 * Created by xeno on 2017/5/22.
 */

public class DeviceRecyclerAdapter extends RecyclerView.Adapter<BluetoothHolder>  {

    public static boolean AUTO_CONNECT = true;

    private Context mContext;
    private OnDeviceSelectListener mListener;

    private List<BleDevice> dataList = new ArrayList<>();

    public DeviceRecyclerAdapter(OnDeviceSelectListener listener) {
        mListener = listener;
    }

    public void setDataList(List<BleDevice> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataToList(BleDevice scanResult) {
        dataList.add(scanResult);
        notifyDataSetChanged();
    }

    @Override
    public BluetoothHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_bluetooth, null);
        BluetoothHolder holder = new BluetoothHolder(view);

        mContext = parent.getContext();

        return holder;
    }

    @Override
    public void onBindViewHolder(BluetoothHolder holder, int position) {
        final BleDevice scanResult = dataList.get(position);

        holder.getNameView().setText(scanResult.getDevice().getName());
        holder.getAddressView().setText(scanResult.getDevice().getAddress());

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                connectDevice(scanResult, AUTO_CONNECT);
                mListener.onSelect(scanResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnDeviceSelectListener {
        void onSelect(BleDevice scanResult);
    }
}
