package me.xeno.unengtrainer.view.adapter;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clj.fastble.data.ScanResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.model.ConnectionWrapper;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.holder.BluetoothHolder;
import me.xeno.unengtrainer.view.holder.ServiceHolder;

/**
 * Created by xeno on 2017/5/22.
 */

@Deprecated
public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceHolder>  {

    public static boolean AUTO_CONNECT = true;

    private Context mContext;

    private List<BluetoothGattService> dataList = new ArrayList<>();

    public void setDataList(List<BluetoothGattService> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataToList(BluetoothGattService bluetoothGattService) {
        dataList.add(bluetoothGattService);
        notifyDataSetChanged();
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_bluetooth, null);
        ServiceHolder holder = new ServiceHolder(view);

        mContext = parent.getContext();

        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
        final BluetoothGattService bluetoothGattService = dataList.get(position);

        holder.getAddressView().setText(bluetoothGattService.getUuid().toString());

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
