package me.xeno.unengtrainer.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xeno.unengtrainer.R;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class BluetoothFragment extends Fragment {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private OnConnectSuccessListener mCallback;

    private TextView mConnectBtn;

    private TextView mDescription;

    public static BluetoothFragment newInstance() {
        return new BluetoothFragment();
    }

    public BluetoothFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnConnectSuccessListener) context;
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
                mCallback.onConnect();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        mConnectBtn = (TextView) root.findViewById(R.id.connect_btn);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    public interface OnConnectSuccessListener {
        void onConnect();
    }


}