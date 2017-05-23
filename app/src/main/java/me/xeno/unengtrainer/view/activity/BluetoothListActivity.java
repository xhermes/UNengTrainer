package me.xeno.unengtrainer.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.clj.fastble.data.ScanResult;
import com.clj.fastble.scan.ListScanCallback;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.model.BleDevice;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.util.ActivityUtils;
import me.xeno.unengtrainer.util.CommonUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.view.adapter.BluetoothListAdapter;
import me.xeno.unengtrainer.view.fragment.BluetoothDisabledFragment;
import me.xeno.unengtrainer.view.fragment.DeviceListFragment;
import me.xeno.unengtrainer.widget.EmptyRecyclerView;

/**
 * Created by Administrator on 2017/5/21.
 */

public class BluetoothListActivity extends BaseActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private Toolbar mToolbar;

    private BluetoothDisabledFragment mBlueToothDisabledFragment;
    private DeviceListFragment mDeviceListFragment;

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

        if(DataManager.getInstance().getBleManager().isBlueEnable()) {
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
                checkLocationPermissionV23();
                scanForDeviceList();
            }
        });
    }

    public void showDeviceListFragment() {
        if (mDeviceListFragment == null) {
            mDeviceListFragment = DeviceListFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mDeviceListFragment, R.id.frame_content);
    }

    public void showBlueToothDisabledFragment() {
        if (mBlueToothDisabledFragment == null) {
            mBlueToothDisabledFragment = BluetoothDisabledFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mBlueToothDisabledFragment, R.id.frame_content);
    }

    private void scanForDeviceList() {
        if(DataManager.getInstance().getBleManager().isBlueEnable()) {
            scanDevice();
        } else {
            //request enable bluetooth
            DataManager.getInstance().getBleManager().enableBluetooth();
        }
    }

    private void scanDevice() {
        mFloatingBtn.startAnimation(mRotateAnimation);

        BluetoothModel model = new BluetoothModel();
        model.scanForDevices().subscribe(new Observer<BleDevice>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mScanDisposable = d;
            }

            @Override
            public void onNext(@NonNull BleDevice device) {
                Logger.debug("onNext" + device.getName());
                mDeviceListFragment.addDeviceToList(device);
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


    public void checkLocationPermissionV23() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permission = 0;

            permission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            Logger.info("permission=" + permission);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermissionV23();
            } else {

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestLocationPermissionV23() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Logger.info("onRequestPermissionsResult granted=" + granted);
        }
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
        if(CommonUtils.isNotNull(mScanDisposable) && !mScanDisposable.isDisposed())
            mScanDisposable.dispose();
    }

    public static void goFromActivity(WeakReference<BaseActivity> reference) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, BluetoothListActivity.class);
            activity.startActivity(intent);
            setJumpingAnim(activity, BaseActivity.ANIM_RIGHT_IN_LEFT_OUT);
        }
    }
}
