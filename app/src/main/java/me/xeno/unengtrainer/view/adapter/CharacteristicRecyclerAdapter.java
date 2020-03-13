//package me.xeno.unengtrainer.view.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import me.xeno.unengtrainer.R;
//import me.xeno.unengtrainer.view.holder.BluetoothHolder;
//
///**
// * Created by xeno on 2017/5/22.
// */
//
//public class CharacteristicRecyclerAdapter extends RecyclerView.Adapter<BluetoothHolder>  {
//
//    public static boolean AUTO_CONNECT = true;
//
//    private Context mContext;
//
//    private List<ScanResult> dataList = new ArrayList<>();
//
//    public void setDataList(List<ScanResult> dataList) {
//        this.dataList = dataList;
//        notifyDataSetChanged();
//    }
//
//    public void addDataToList(ScanResult scanResult) {
//        dataList.add(scanResult);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public BluetoothHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_bluetooth, null);
//        BluetoothHolder holder = new BluetoothHolder(view);
//
//        mContext = parent.getContext();
//
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(BluetoothHolder holder, int position) {
//        final ScanResult scanResult = dataList.get(position);
//
//        holder.getNameView().setText(scanResult.getDevice().getName());
//        holder.getAddressView().setText(scanResult.getDevice().getAddress());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//
//}
