package me.xeno.unengtrainer.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.view.activity.BaseActivity;
import me.xeno.unengtrainer.view.activity.BluetoothListActivity;
import me.xeno.unengtrainer.view.activity.TestActivity;


public class BluetoothDisabledFragment extends Fragment {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private BluetoothDisabledListener mCallback;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (BluetoothDisabledListener) context;
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
                mCallback.onConnectBtnClick();
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

    public interface BluetoothDisabledListener {
        void onConnectBtnClick();
    }


}