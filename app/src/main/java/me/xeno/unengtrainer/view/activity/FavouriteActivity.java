package me.xeno.unengtrainer.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.listener.OnFavItemSelectListener;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.presenter.FavouritePresenter;
import me.xeno.unengtrainer.view.adapter.FavouriteRecyclerAdapter;

/**
 * Created by Administrator on 2017/5/14.
 */

public class FavouriteActivity extends BaseActivity implements OnFavItemSelectListener {

    public static final int REQUEST_CODE_FAVOURITE = 2;

    private FavouritePresenter mPresenter;

    private Toolbar mToolbar;

    private LRecyclerView mRecyclerView;
    private View mEmptyView;

    private FavouriteRecyclerAdapter mAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_favourite);
    }

    @Override
    protected void findViewById() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (LRecyclerView) findViewById(R.id.recycler);
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setEmptyView(mEmptyView);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin); //设置下拉刷新Progress的样式
//        mRecyclerView.setArrowImageView(R.drawable.ic_refresh);
        mAdapter = new FavouriteRecyclerAdapter(this);
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

    }

    public void updateRecycler(List<FavouriteRecord> dataList) {
        mAdapter.setDataList(dataList);
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
    public void onSelect(long id) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
