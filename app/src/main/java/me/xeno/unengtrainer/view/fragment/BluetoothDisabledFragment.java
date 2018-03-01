package me.xeno.unengtrainer.view.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.presenter.MainPresenter;


public class BluetoothDisabledFragment extends BaseMainFragment {

    public static final int REQUEST_ENABLE_BLUETOOTH = 1;
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
                if(Config.DEBUG_MODE == Config.DEBUG_MODE_CTRL) {
                    //调试模式时直接不连蓝牙，进入主控界面
                    getMainActivity().showMainControlFragment();
                } else {
                    //检查有没有开启蓝牙，如果开启了，直接切换到DeviceRecyclerFragment
                    //否则请求开启蓝牙，开启成功后在onActivityResult中切换到DeviceRecyclerFragment
                    checkPermissionAndEnableBluetooth();
                }


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

    public void checkPermissionAndEnableBluetooth() {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!DataManager.getInstance().getBleManager().isBlueEnable()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            getMainActivity().showDeviceRecyclerFragment();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
            return;
        } else {
            getMainActivity().showDeviceRecyclerFragment();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}