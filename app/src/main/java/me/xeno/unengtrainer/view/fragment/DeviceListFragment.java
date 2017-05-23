package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private EmptyRecyclerView mRecyclerView;
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
            mRootView = inflater.inflate(R.layout.fragment_shortcut, container, false);
        }
        mRecyclerView = (EmptyRecyclerView) mRootView.findViewById(R.id.recycler);

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
        mAdapter = new BluetoothListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addDeviceToList(BleDevice device) {
        mAdapter.addDataToList(device);
    }

}