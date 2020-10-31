package me.xeno.unengtrainer.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.util.CommonUtils;
import me.xeno.unengtrainer.util.RxUtils;

/**
 * Created by Administrator on 2017/5/14.
 */

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_ENTER_TIME = 1;

    private Disposable mTimerDisposable;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        requestPermission();

    }

    @Override
    protected void loadData() {

    }

    private boolean checkBleSupport() {
        return DataManager.getInstance().getBleManager().isSupportBle();
    }

    private void dialogNotSupport() {
        new MaterialDialog.Builder(this)
                .content("当前设备不支持BLE功能")
                .positiveText("退出")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        System.exit(0);
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dispose when exit
        if(CommonUtils.isNotNull(mTimerDisposable))
            mTimerDisposable.dispose();
    }

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("DEBUG", "PERMISSION_GRANTED");
                } else {
                    Log.e("DEBUG", "PERMISSION_DENIED");
                }

                if(checkBleSupport()) {
                    mTimerDisposable = RxUtils.timer(SPLASH_ENTER_TIME).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                            MainActivity.goFromActivity(new WeakReference<BaseActivity>(SplashActivity.this));
                            finish();
                        }
                    });
                } else {
                    dialogNotSupport();
                }

                break;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(SplashActivity.this, "Please grante write storage permission", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}
