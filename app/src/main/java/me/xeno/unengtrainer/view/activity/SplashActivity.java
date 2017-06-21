package me.xeno.unengtrainer.view.activity;

import android.support.annotation.NonNull;

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
}
