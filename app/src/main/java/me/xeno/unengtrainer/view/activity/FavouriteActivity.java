package me.xeno.unengtrainer.view.activity;

import android.content.Intent;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.listener.OnFavItemSelectListener;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.presenter.FavouritePresenter;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.adapter.FavouriteRecyclerAdapter;

/**
 * Created by Administrator on 2017/5/14.
 */

public class FavouriteActivity extends BaseActivity implements OnFavItemSelectListener, ActionMode.Callback {

    public static final int REQUEST_CODE_FAVOURITE = 2;

    private FavouritePresenter mPresenter;

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private View mEmptyView;

    private FavouriteRecyclerAdapter mAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_favourite);
    }

    @Override
    protected void findViewById() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mEmptyView = findViewById(R.id.empty);
    }

    @Override
    protected void initView() {
        mPresenter = new FavouritePresenter(this);

        mToolbar.setTitle("我的收藏");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
    }

    @Override
    protected void loadData() {
        mPresenter.loadFavourites();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        Logger.error("设置LinearLayoutManager");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new LLayoutManager());
//        mRecyclerView.setLayoutManager(new TestLayoutManager(this));
//        mRecyclerView.setPullRefreshEnabled(false);
//        mRecyclerView.setEmptyView(mEmptyView);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin); //设置下拉刷新Progress的样式
//        mRecyclerView.setArrowImageView(R.drawable.ic_refresh);
        Logger.error("创建FavouriteRecyclerAdapter");
        mAdapter = new FavouriteRecyclerAdapter(this);
//        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        Logger.error("设置创建FavouriteRecyclerAdapter");
        mRecyclerView.setAdapter(mAdapter);

    }

    public void updateRecycler(List<FavouriteRecord> dataList) {
        Logger.error("调用updateRecycler()和notifyDataSetChanged()更新列表");
        mAdapter.setDataList(dataList);
        mAdapter.notifyDataSetChanged();
    }

    public static void goFromActivityForResult(WeakReference<BaseActivity> reference) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, FavouriteActivity.class);
            activity.startActivityForResult(intent, REQUEST_CODE_FAVOURITE);
            setJumpingAnim(activity, BaseActivity.ANIM_RIGHT_IN_LEFT_OUT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onSelect(FavouriteRecord record) {

        if(mAdapter.isEditMode())
            return;

        if(Config.isDebugging()){
            ToastUtils.toast(this.getApplicationContext(),
                    "选择收藏：id= " + record.getId() + "，" + record.getSwingAngle()+","+record.getElevationAngle());
        }
        Intent intent = new Intent();
        intent.putExtra("id", record.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemLongClick(FavouriteRecord record) {
        if(!mAdapter.isEditMode()) {
            //打开ActionMode
            Logger.error("打开ActionMode");
            mAdapter.setEditMode(true);
            startSupportActionMode(this);
        }
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_fav_edit, menu);

        mode.setTitle("编辑模式");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        //TODO 选中时显示subTitle（已选中x项）
        switch (item.getItemId()) {
            case R.id.action_select_all:
                Logger.error("全选");
                //通知RecyclerView全选
                selectAll();
                return true;
            case R.id.action_delete:
                //通知RecyclerView删除
                deleteSelected();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Logger.error("onDestroyActionMode");
        //通知RecyclerView恢复item样式
        if(mAdapter.isEditMode())
            mAdapter.setEditMode(false);
        for(FavouriteRecord fr:mAdapter.getDataList()) {
            //退出编辑时，放弃所有选择
            fr.setChecked(false);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        boolean allSelected = true;
        for(FavouriteRecord fr: mAdapter.getDataList()) {
            if(!fr.getChecked())
                allSelected = false;
        }
        for(FavouriteRecord fr: mAdapter.getDataList()) {
            if(allSelected)
                fr.setChecked(false);
            else
                fr.setChecked(true);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void deleteSelected() {

        int first = 0;//标记第一个被修改的元素，减少重绘
        int size = mAdapter.getDataList().size();
        for (int i = 0; i < size; i++) {
            FavouriteRecord fr = mAdapter.getDataList().get(i);
            if (fr.getChecked()) {
                if(first == 0)
                    first = i;
                mAdapter.notifyItemRemoved(i);
                //先删数据库里的记录，不然会报数组越界
                mPresenter.deleteFavouriteFromDb(mAdapter.getDataList().get(i).getId());
                mAdapter.getDataList().remove(i);
                i--;
                size--;//避免数组越界
            }
        }

            mAdapter.notifyItemRangeChanged(first, mAdapter.getDataList().size() - first);

    }


}
