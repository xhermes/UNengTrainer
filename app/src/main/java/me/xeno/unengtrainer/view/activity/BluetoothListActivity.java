package me.xeno.unengtrainer.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.clj.fastble.data.ScanResult;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.receiver.BluetoothReceiver;
import me.xeno.unengtrainer.util.ActivityUtils;
import me.xeno.unengtrainer.util.CommonUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.view.fragment.BluetoothDisabledFragment;
import me.xeno.unengtrainer.view.fragment.CharacteristicListFragment;
import me.xeno.unengtrainer.view.fragment.DeviceRecyclerFragment;
import me.xeno.unengtrainer.view.fragment.ServiceListFragment;

/**
 * Created by Administrator on 2017/5/21.
 */

public class BluetoothListActivity extends BaseActivity
        implements BluetoothDisabledFragment.BluetoothDisabledListener,
        BluetoothReceiver.OnBluetoothStateChangeListener,
        DeviceRecyclerFragment.OnDeviceSelectListener{

    public static final int REQUEST_CODE = 1;

    private BluetoothReceiver mBluetoothReceiver;

    private Toolbar mToolbar;

    private BluetoothDisabledFragment mBlueToothDisabledFragment;
    private DeviceRecyclerFragment mDeviceListFragment;
    private ServiceListFragment mServiceListFragment;
    private CharacteristicListFragment mCharacteristicListFragment;

    private FloatingActionButton mFloatingBtn;

    private Animation mRotateAnimation;

    private Disposable mScanDisposable;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_list_bluetooth);
    }

    @Override
    protected void findViewById() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mFloatingBtn = (FloatingActionButton) findViewById(R.id.refresh);
    }

    @Override
    protected void initView() {
        mRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.view_rotate_360);

        mToolbar.setTitle("连接管理");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFloatingActionBar();

        initBluetoothReceiver();

        if (DataManager.getInstance().getBleManager().isBlueEnable()) {
            showDeviceListFragment();
//            scanForDeviceList();
        } else {
            showBlueToothDisabledFragment();
        }
    }

    @Override
    protected void loadData() {

    }

    private void initFloatingActionBar() {
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanForDeviceList();
            }
        });
    }

    private void initBluetoothReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mBluetoothReceiver = new BluetoothReceiver(this);
        registerReceiver(mBluetoothReceiver, intentFilter);
    }

    public void showDeviceListFragment() {
        if (mDeviceListFragment == null) {
            mDeviceListFragment = DeviceRecyclerFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mDeviceListFragment, R.id.frame_content);
    }

    public void showBlueToothDisabledFragment() {
        if (mBlueToothDisabledFragment == null) {
            mBlueToothDisabledFragment = BluetoothDisabledFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mBlueToothDisabledFragment, R.id.frame_content);
    }

    public void showServiceRecyclerFragment() {
        if (mServiceListFragment == null) {
//            mServiceListFragment = ServiceListFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mServiceListFragment, R.id.frame_content);
    }

    public void showCharacteristicRecyclerFragment() {
        if (mCharacteristicListFragment == null) {
            mCharacteristicListFragment = CharacteristicListFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mCharacteristicListFragment, R.id.frame_content);
    }

    private void scanForDeviceList() {
        if (DataManager.getInstance().getBleManager().isBlueEnable()) {
            scanDevice();
        } else {
            //request enable bluetooth
            DataManager.getInstance().getBleManager().enableBluetooth();
        }
    }

    private void scanDevice() {
        Logger.info("scanDevice---->");
        mFloatingBtn.startAnimation(mRotateAnimation);

        BluetoothModel model = new BluetoothModel();
        model.scanForDevices().subscribe(new Observer<ScanResult>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mScanDisposable = d;
            }

            @Override
            public void onNext(@NonNull ScanResult scanResult) {
                Logger.info("onNext" + scanResult.getDevice().getName());
                mDeviceListFragment.addDeviceToList(scanResult);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mFloatingBtn.clearAnimation();
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                mFloatingBtn.clearAnimation();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CommonUtils.isNotNull(mScanDisposable) && !mScanDisposable.isDisposed())
            mScanDisposable.dispose();
        unregisterReceiver(mBluetoothReceiver);
    }

    public static void goFromActivityForResult(WeakReference<BaseActivity> reference, int requestCode) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, BluetoothListActivity.class);
            activity.startActivityForResult(intent, requestCode);
            setJumpingAnim(activity, BaseActivity.ANIM_RIGHT_IN_LEFT_OUT);
        }
    }

    @Override
    public void onConnectBtnClick() {
        requestEnableBluetooth();
    }

    public void requestEnableBluetooth() {
        DataManager.getInstance().getBleManager().enableBluetooth();
    }

    @Override
    public void onBluetoothTurnOff() {
        showBlueToothDisabledFragment();
    }
    @Override
    public void onBluetoothTurnOn() {
        showDeviceListFragment();
    }

    @Override
    public void onSelect(ScanResult scanResult) {
        Intent intent = new Intent();
        intent.putExtra("scanResult", scanResult);
        setResult(RESULT_OK, intent);
        finish();
    }
}
