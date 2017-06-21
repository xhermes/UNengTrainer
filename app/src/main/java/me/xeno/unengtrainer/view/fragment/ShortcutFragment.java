package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.view.adapter.ShortcutAdapter;
import me.xeno.unengtrainer.view.holder.ShortcutHolder;
import me.xeno.unengtrainer.widget.EmptyRecyclerView;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class ShortcutFragment extends BaseMainFragment {

    private View mRootView;

    private EmptyRecyclerView mRecyclerView;
    private ShortcutAdapter mAdapter;

    public static ShortcutFragment newInstance() {
        return new ShortcutFragment();
    }

    public ShortcutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_shortcut, container, false);
        }
        mRecyclerView = (EmptyRecyclerView) mRootView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mAdapter = new ShortcutAdapter();
        mRecyclerView.setAdapter(mAdapter);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return mRootView;
    }

}