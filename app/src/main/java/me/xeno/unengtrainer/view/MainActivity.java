package me.xeno.unengtrainer.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.util.ActivityUtils;
import me.xeno.unengtrainer.view.adapter.MainPagerAdapter;
import me.xeno.unengtrainer.view.adapter.ShortcutAdapter;
import me.xeno.unengtrainer.view.fragment.BluetoothFragment;
import me.xeno.unengtrainer.view.fragment.MainControlFragment;
import me.xeno.unengtrainer.view.fragment.ShortcutFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BluetoothFragment.OnConnectSuccessListener {

    private MainControlFragment mMainControlFragment;
    private BluetoothFragment mBlueToothFragment;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected void initView() {

        mToolbar.setTitle("未连接");
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

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
//        BluetoothFragment bluetoothFragment =
//                (BluetoothFragment) getSupportFragmentManager().findFragmentById(
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
        if (mBlueToothFragment == null) {
            mBlueToothFragment = BluetoothFragment.newInstance();
        }
        ActivityUtils.replaceFragment(getSupportFragmentManager(), mBlueToothFragment, R.id.frame_content);
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
        int id = item.getItemId();

        if (id == R.id.nav_data) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onConnect() {
        showMainControlFragment();
        setmBottomBarVisibility(View.VISIBLE);
        mToolbar.setTitle("机器00001");

    }
}
