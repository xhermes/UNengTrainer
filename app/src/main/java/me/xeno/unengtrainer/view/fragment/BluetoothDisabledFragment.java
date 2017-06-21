package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xeno.unengtrainer.R;


public class BluetoothDisabledFragment extends BaseMainFragment {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private TextView mConnectBtn;

    private TextView mDescription;

    public static BluetoothDisabledFragment newInstance() {
        return new BluetoothDisabledFragment();
    }

    public BluetoothDisabledFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        FloatingActionButton fab =
//                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
//        fab.setImageResource(R.drawable.ic_done);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 检查有没有开启蓝牙

                //切换到DeviceRecyclerFragment
               getMainActivity().showDeviceRecyclerFragment();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bluetooth_disabled, container, false);
        mConnectBtn = (TextView) root.findViewById(R.id.connect_btn);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }


}