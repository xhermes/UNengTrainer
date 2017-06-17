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
                //TODO 显示characteristic列表
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void connectDevice(ScanResult scanResult, boolean autoConnect) {
        Logger.info("connectDevice()-------------->" + scanResult.getDevice().getAddress());

        BluetoothModel model = new BluetoothModel();
        model.connect(mContext, scanResult, autoConnect)
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
                    case ConnectionWrapper.MSG_CODE_SERVICES_DISCOVERD:
                        //TODO
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
