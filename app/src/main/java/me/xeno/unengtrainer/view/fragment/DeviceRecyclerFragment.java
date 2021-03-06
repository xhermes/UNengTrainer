package me.xeno.unengtrainer.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.clj.fastble.data.ScanResult;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.adapter.DeviceRecyclerAdapter;
import me.xeno.unengtrainer.widget.EmptyRecyclerView;

/**
 * show discovered device list.
 */
public class DeviceRecyclerFragment extends BaseMainFragment implements DeviceRecyclerAdapter.OnDeviceSelectListener {

    private View mRootView;

    private EmptyRecyclerView mRecyclerView;
    private DeviceRecyclerAdapter mAdapter;
    private View mEmptyView;

    private Disposable mSearchingDisposable;

    public static DeviceRecyclerFragment newInstance() {
        DeviceRecyclerFragment fragment = new DeviceRecyclerFragment();
        return fragment;
    }

    public DeviceRecyclerFragment() {
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
            mRootView = inflater.inflate(R.layout.fragment_list_device, container, false);
        }
        mRecyclerView = (EmptyRecyclerView) mRootView.findViewById(R.id.recycler);
        mEmptyView = mRootView.findViewById(R.id.empty);
        mRecyclerView.setEmptyView(mEmptyView);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        startSearching();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recycler_device, menu);
        super.onCreateOptionsMenu(menu, inflater);

        //右上角按钮
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startSearching();
                break;
        }
        return true;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setEmptyView(mEmptyView);//TODO 增加emptyView
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin); //设置下拉刷新Progress的样式
//        mRecyclerView.setArrowImageView(R.drawable.ic_refresh);
        mAdapter = new DeviceRecyclerAdapter(this);
//        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void startSearching() {
        if(Config.isDebugging())
            ToastUtils.toast(getActivity().getApplication(), "尝试开始搜索蓝牙设备，任务状态："
                     + mSearchingDisposable == null?"当前没有搜索任务":"当前搜索任务已经在执行");

        if(mSearchingDisposable != null && !mSearchingDisposable.isDisposed()) {
            Logger.warning("任务已经在执行");
            return;
        }

        getMainActivity().getPresenter()
                .scanForDevices().subscribe(new Observer<ScanResult>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                //TODO dispose searching task when fragment exits
                mSearchingDisposable = d;
            }

            @Override
            public void onNext(@NonNull ScanResult scanResult) {
                addDeviceToList(scanResult);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                dispose();
            }

            @Override
            public void onComplete() {
                dispose();
            }
        });
    }

    public void addDeviceToList(ScanResult scanResult) {
        mAdapter.addDataToList(scanResult);
    }

    public void dispose() {
        if(mSearchingDisposable != null && !mSearchingDisposable.isDisposed()){
            mSearchingDisposable.dispose();
        }
    }

    @Override
    public void onSelect(ScanResult scanResult) {
        Logger.info("onSelect()");
        //TODO
        getMainActivity().getPresenter().bindService(scanResult);
    }
}