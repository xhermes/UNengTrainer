package me.xeno.unengtrainer.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import java.lang.ref.WeakReference;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.view.adapter.BluetoothListAdapter;
import me.xeno.unengtrainer.widget.EmptyRecyclerView;

/**
 * Created by Administrator on 2017/5/21.
 */

public class BluetoothListActivity extends BaseActivity {

    private Toolbar mToolbar;

    private EmptyRecyclerView mRecyclerView;

    private BluetoothListAdapter mAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_list_bluetooth);
    }

    @Override
    protected void findViewById() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler);
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setTitle("连接管理");

        initRecyclerView();
    }

    @Override
    protected void loadData() {

    }

    private void initRecyclerView() {
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BluetoothListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void goFromActivity(WeakReference<BaseActivity> reference) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, BluetoothListActivity.class);
            activity.startActivity(intent);
            setJumpingAnim(activity, BaseActivity.ANIM_FADE_IN_FADE_OUT);
        }
    }
}
