package me.xeno.unengtrainer.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.util.ActivityUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.adapter.ShortcutAdapter;
import me.xeno.unengtrainer.view.fragment.BluetoothDisabledFragment;
import me.xeno.unengtrainer.view.fragment.MainControlFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BluetoothDisabledFragment.BluetoothDisabledListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private MainControlFragment mMainControlFragment;
    private BluetoothDisabledFragment mBlueToothDisabledFragment;

    private FloatingActionButton mFloatingActionBtn;
    private Toolbar mToolbar;
    //    private TabLayout mTabLayout;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private View mBottomActionBar;
    private View mFavouriteBtn;
    private View mSettingBtn;

//    private ViewPager mViewPager;
//    private MainPagerAdapter mPagerAdapter;

//    private List<Fragment> mFragmentList = new ArrayList<>();

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
    }

    @Override
    protected void initView() {
        checkLocationPermissionV23();

        mToolbar.setTitle("未连接");
        setSupportActionBar(mToolbar);

        initDrawer();

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
        setmBottomBarVisibility(View.GONE);

//        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
//        mViewPager.setAdapter(mPagerAdapter);

    }

    public void setmBottomBarVisibility(int visibility) {
        mBottomActionBar.setVisibility(visibility);
    }

    public void showMainControlFragment() {
        if (mMainControlFragment == null) {
            mMainControlFragment = MainControlFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mMainControlFragment, R.id.frame_content);
    }

    public void showBlueToothFragment() {
        if (mBlueToothDisabledFragment == null) {
            mBlueToothDisabledFragment = BluetoothDisabledFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mBlueToothDisabledFragment, R.id.frame_content);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            Toast.makeText(this, "置零", Toast.LENGTH_LONG).show();
            return true;
        }
//        if (id == R.id.action_favorite) {
//            Toast.makeText(this, "收藏", Toast.LENGTH_LONG).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        mDrawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
//        if (id == R.id.nav_bluetooth) {
//            BluetoothListActivity.goFromActivity(new WeakReference<BaseActivity>(MainActivity.this));
//        }
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

    public static void goFromActivity(WeakReference<BaseActivity> reference) {
        BaseActivity activity = reference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            setJumpingAnim(activity, BaseActivity.ANIM_FADE_IN_FADE_OUT);
        }
    }

    @Override
    public void onConnectBtnClick() {
        BluetoothListActivity.goFromActivityForResult(new WeakReference<BaseActivity>(this), BluetoothListActivity.REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == BluetoothListActivity.REQUEST_CODE) {
                //得到选中的device，连接
            }
        }
    }

}
