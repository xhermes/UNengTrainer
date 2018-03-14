package me.xeno.unengtrainer.view.fragment;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.view.adapter.ServiceRecyclerAdapter;

/**
 * 在DeviceListFragment中选中BLE设备后，不需要再进入service和characteristic列表选择，直接使用拟定好的UUID找到
 * 对应机器发送与接收指令的service和characteristic
 * @deprecated
 */

public class ServiceListFragment extends Fragment {

    private View mRootView;

    private RecyclerView mRecyclerView;
    private ServiceRecyclerAdapter mAdapter;

    private BluetoothGatt mBluetoothGatt;

    public static ServiceListFragment newInstance(BluetoothGatt bluetoothGatt) {
        ServiceListFragment fragment = new ServiceListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("bluetoothGatt", bluetoothGatt);
//        //fragment保存参数，传入一个Bundle对象
//        fragmentOne.setArguments(bundle);
//        return fragmentOne;
        return new ServiceListFragment();
    }

    public ServiceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_list_device, container, false);
        }
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setPullRefreshEnabled(false);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin); //设置下拉刷新Progress的样式
//        mRecyclerView.setArrowImageView(R.drawable.ic_refresh);
        mAdapter = new ServiceRecyclerAdapter();
//        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void addServiceToList(BluetoothGattService bluetoothGattService) {
        mAdapter.addDataToList(bluetoothGattService);
    }

}