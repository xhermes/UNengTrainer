package me.xeno.unengtrainer.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.lang.ref.WeakReference;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.view.adapter.FavouriteRecyclerAdapter;

/**
 * Created by Administrator on 2017/5/14.
 */

public class FavouriteActivity extends BaseActivity {

    private LRecyclerView mRecyclerView;
    private View mEmptyView;

    private FavouriteRecyclerAdapter mAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_favourite);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = (LRecyclerView) findViewById(R.id.recycler);
        mEmptyView = findViewById(R.id.empty);
    }

    @Override
    protected void initView() {
        initRecyclerView();
    }

    @Override
    protected void loadData() {

    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setEmptyView(mEmptyView);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin); //设置下拉刷新Progress的样式
//        mRecyclerView.setArrowImageView(R.drawable.ic_refresh);
        mAdapter = new FavouriteRecyclerAdapter();
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

        FavouriteRecord record = new FavouriteRecord();
        record.setName("gweaga");
        record.setModifyTime("gweagwag");
        record.setElevationAngle(12);
        record.setLeftMotorSpeed(12);
        mAdapter.addDataToList(record);
    }

    public static void goFromActivity(WeakReference<BaseActivity> reference) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, FavouriteActivity.class);
            activity.startActivity(intent);
            setJumpingAnim(activity, BaseActivity.ANIM_RIGHT_IN_LEFT_OUT);
        }
    }

}
