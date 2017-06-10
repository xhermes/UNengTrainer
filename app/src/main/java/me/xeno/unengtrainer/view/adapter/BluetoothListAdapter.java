package me.xeno.unengtrainer.view.adapter;

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
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.model.BleDevice;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.model.ConnectionWrapper;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.holder.BluetoothHolder;

/**
 * Created by xeno on 2017/5/22.
 */

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothHolder>  {

    public static boolean AUTO_CONNECT = true;

    private Context mContext;

    private List<ScanResult> dataList = new ArrayList<>();

    public void setDataList(List<ScanResult> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataToList(ScanResult scanResult) {
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
        final ScanResult scanResult = dataList.get(position);

        holder.getNameView().setText(scanResult.getDevice().getName());
        holder.getAddressView().setText(scanResult.getDevice().getAddress());

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDevice(scanResult, AUTO_CONNECT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void connectDevice(ScanResult scanResult, boolean autoConnect) {


        BluetoothModel model = new BluetoothModel();
        model.connect(scanResult, autoConnect)
                .subscribe(new Observer<ConnectionWrapper>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ConnectionWrapper connectionWrapper) {
                switch (connectionWrapper.getMsgCode()) {
                    case ConnectionWrapper.MSG_CODE_DEVICE_NOT_FOUND:
                        ToastUtils.toast(mContext, connectionWrapper.getMessage());
                        break;
                    case ConnectionWrapper.MSG_CODE_DEVICE_FOUND:

                        break;
                    case ConnectionWrapper.MSG_CODE_CONNECT_FAIL:
                        break;
                    case ConnectionWrapper.MSG_CODE_CONNECT_SUCCESS:
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
