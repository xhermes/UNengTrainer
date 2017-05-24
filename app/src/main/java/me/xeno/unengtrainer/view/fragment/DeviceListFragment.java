package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.model.BleDevice;
import me.xeno.unengtrainer.view.adapter.BluetoothListAdapter;
import me.xeno.unengtrainer.view.adapter.ShortcutAdapter;
import me.xeno.unengtrainer.widget.EmptyRecyclerView;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class DeviceListFragment extends Fragment {

    private View mRootView;

    private LRecyclerView mRecyclerView;
    private BluetoothListAdapter mAdapter;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    public DeviceListFragment() {
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
        mAdapter = new BluetoothListAdapter();
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

    }

    public void addDeviceToList(BleDevice device) {
        mAdapter.addDataToList(device);
    }

}