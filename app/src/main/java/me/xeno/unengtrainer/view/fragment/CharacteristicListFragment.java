package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clj.fastble.data.ScanResult;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.view.adapter.DeviceRecyclerAdapter;

/**
 * 在DeviceListFragment中选中BLE设备后，不需要再进入service和characteristic列表选择，直接使用拟定好的UUID找到
 * 对应机器发送与接收指令的service和characteristic
 * @deprecated
 */
public class CharacteristicListFragment extends Fragment {

    private View mRootView;

    private LRecyclerView mRecyclerView;
    private DeviceRecyclerAdapter mAdapter;

    public static CharacteristicListFragment newInstance() {
        return new CharacteristicListFragment();
    }

    public CharacteristicListFragment() {
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
        mRecyclerView = (LRecyclerView) mRootView.findViewById(R.id.recycler);

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
        mRecyclerView.setPullRefreshEnabled(false);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin); //设置下拉刷新Progress的样式
//        mRecyclerView.setArrowImageView(R.drawable.ic_refresh);
        mAdapter = new DeviceRecyclerAdapter();
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

    }

    public void addDeviceToList(ScanResult scanResult) {
        mAdapter.addDataToList(scanResult);
    }

}