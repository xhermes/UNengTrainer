package me.xeno.unengtrainer.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.ref.WeakReference;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.presenter.MainPresenter;
import me.xeno.unengtrainer.util.ActivityUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.NumberUtils;
import me.xeno.unengtrainer.view.adapter.ShortcutAdapter;
import me.xeno.unengtrainer.view.fragment.BluetoothDisabledFragment;
import me.xeno.unengtrainer.view.fragment.DeviceRecyclerFragment;
import me.xeno.unengtrainer.view.fragment.MainControlFragment;
import me.xeno.unengtrainer.widget.DashboardView;
import me.xeno.unengtrainer.widget.LoadingDialog;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //TODO 目前流程 搜索设备->点选设备->bindService()->onServiceConnected()
    //TODO ->调用connect()，同时切换到MainControlFragment

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private MainPresenter mPresenter;

    private MainControlFragment mMainControlFragment;
    private BluetoothDisabledFragment mBlueToothDisabledFragment;
    private DeviceRecyclerFragment mDeviceRecyclerFragment;

//    private FloatingActionButton mFloatingActionBtn;
    private Toolbar mToolbar;
    //    private TabLayout mTabLayout;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private View mBottomActionBar;
    private View mFavouriteBtn;
    private View mSettingBtn;

    private DashboardView mDashboardView;

    private Dialog mRequestMakeZeroDialog;

    public Dialog getRequestMakeZeroDialog() {
        return mRequestMakeZeroDialog;
    }

    public void showRequestMakeZeroDialog() {
        mRequestMakeZeroDialog = new MaterialDialog.Builder(this)
                .icon(this.getResources().getDrawable(R.drawable.ic_make_zero))
                .title("自动校准")
                .content("确认机器将自动校准零位，可以在左侧弹出菜单->设置->校准零位，主动发起校准。")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .negativeText("手动调试")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                        //TODO 校准零位时增加loading对话框，getStatus()回调校准成功以后才dismiss
                        mPresenter.grantMakingZero();
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                        getPresenter().setIgnoreMakeZero(true);
                        dialog.dismiss();
                    }
                }).build();

                mRequestMakeZeroDialog.show();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void findViewById() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
//        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mBottomActionBar = findViewById(R.id.bottom_action_bar);
        mFavouriteBtn = findViewById(R.id.favourite_btn);

        mDashboardView = (DashboardView) findViewById(R.id.dashboard);
    }

    @Override
    protected void initView() {
        mPresenter = new MainPresenter(this);

        checkLocationPermissionV23();

        mToolbar.setTitle("未连接");
        setSupportActionBar(mToolbar);

        initDrawer();


//        LoadingDialog d= new LoadingDialog(this);
//        d.show();

        mNavigationView.setNavigationItemSelectedListener(this);
        mFavouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("收藏")
                        // second parameter is an optional layout manager. Must be a LinearLayoutManager or GridLayoutManager.
                        .adapter(new ShortcutAdapter(), null)
                        .show();
            }
        });

        prepareFragments();
//        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    protected void loadData() {
    }

    private void prepareFragments() {
//        MainControlFragment fragment1 = new MainControlFragment();
//        ShortcutFragment fragment2 = new ShortcutFragment();
//
//        mFragmentList.add(fragment1);
//        mFragmentList.add(fragment2);
//
//        BluetoothDisabledFragment bluetoothFragment =
//                (BluetoothDisabledFragment) getSupportFragmentManager().findFragmentById(
//                        R.id.frame_content);

        showBlueToothFragment();
        setBottomBarVisibility(View.GONE);
//        mNavigationView.setCheckedItem(R.id.nav_bluetooth);
//        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
//        mViewPager.setAdapter(mPagerAdapter);

    }

    public void setBottomBarVisibility(int visibility) {
        mBottomActionBar.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //TODO 改成点两次返回退出app
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        mDrawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_favourite:
                FavouriteActivity.goFromActivityForResult(new WeakReference<BaseActivity>(MainActivity.this));

                break;
            case R.id.nav_setting:
                //TODO 新建设置界面
                getPresenter().grantMakingZero();
                break;
        }
        return false;
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

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
       mPresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FavouriteActivity.REQUEST_CODE_FAVOURITE && resultCode == RESULT_OK) {
            long id = data.getLongExtra("id", 0);
            mMainControlFragment.selectFromFavourite(id);
        }
    }

    public void displayAngle(String angle1, String angle2) {
        mDashboardView.setCurrentSwingAngle(NumberUtils.showNumber1Decimal(angle1));
        mDashboardView.setCurrentElevationAngle(NumberUtils.showNumber1Decimal(angle2));
    }

    public void displaySpeed(String leftSpeed, String rightSpeed) {
        mDashboardView.setCurrentLeftSpeed(Float.valueOf(leftSpeed));
        mDashboardView.setCurrentRightSpeed(Float.valueOf(rightSpeed));
    }

//    public void stopGetCurrentAngle() {
//        mMainControlFragment.stopGetCurrentAngle();
//    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void displayBattery(String voltage) {
        mDashboardView.showCurrentVoltage(voltage);
    }

//    public void refreshCurrentSpeed(int left, int right) {
//        mDashboardView.setCurrentLeftSpeed(left);
//        mDashboardView.setCurrentRightSpeed(right);
//    }

    public void showMainControlFragment() {
        mDashboardView.setVisibility(View.VISIBLE);
        if (mMainControlFragment == null) {
            mMainControlFragment = MainControlFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mMainControlFragment, R.id.frame_content);
        mPresenter.setOnGetMotorSpeedListener(mMainControlFragment);
    }
    public void showBlueToothFragment() {
        mDashboardView.setVisibility(View.GONE);
        if (mBlueToothDisabledFragment == null) {
            mBlueToothDisabledFragment = BluetoothDisabledFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mBlueToothDisabledFragment, R.id.frame_content);
    }
    public void showDeviceRecyclerFragment() {
        if (mDeviceRecyclerFragment == null) {
            mDeviceRecyclerFragment = DeviceRecyclerFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mDeviceRecyclerFragment, R.id.frame_content);
    }

    public static void goFromActivity(WeakReference<BaseActivity> reference) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            setJumpingAnim(activity, BaseActivity.ANIM_FADE_IN_FADE_OUT);
        }
    }

    public MainPresenter getPresenter() {
        return mPresenter;
    }

}
