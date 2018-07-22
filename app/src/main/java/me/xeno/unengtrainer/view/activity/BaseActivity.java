package me.xeno.unengtrainer.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.presenter.MainPresenter;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.widget.LoadingDialog;

/**
 * Created by Administrator on 2017/5/14.
 */

public abstract class BaseActivity extends AppCompatActivity {

    LoadingDialog mLoadingDialog;

    public static final int ANIM_LEFT_IN_RIGHT_OUT = 1;
    public static final int ANIM_RIGHT_IN_LEFT_OUT = 2;
    public static final int ANIM_FADE_IN_FADE_OUT = 3;
    public static final int ANIM_TOP_IN_BOTTOM_OUT = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            init();
    }

    protected abstract void setContentView();

    protected abstract void findViewById();

    protected void init() {
        Logger.debug(this.getLocalClassName());
        setContentView();
        findViewById();
        Logger.debug("findViewById finished");
        initView();
        Logger.debug("initView finished");
        loadData();
        Logger.debug("loadData finished");
    }

    protected abstract void initView();

    protected abstract void loadData();

    public void showLoadingDialog() {
        if(mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(this);
        if(!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if(mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    protected static void setJumpingAnim(Activity activity, int animMode) {
        switch (animMode) {
            case ANIM_RIGHT_IN_LEFT_OUT:
                activity.overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_left_out);
                break;
            case ANIM_LEFT_IN_RIGHT_OUT:
                activity.overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_right_out);
                break;
            case ANIM_FADE_IN_FADE_OUT:
                activity.overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
                break;
            case ANIM_TOP_IN_BOTTOM_OUT:
                activity.overridePendingTransition(R.anim.push_top_in,
                        R.anim.push_bottom_out);
                break;
//            case ANIM_LEFT_OUT_RIGHT_IN:
//                activity.overridePendingTransition(R.anim.push_top_right_in,
//                        0);
//                break;
//            case ANIM_FADE_OUT_FADE_IN:
//                activity.overridePendingTransition(R.anim.push_top_right_in,
//                        0);
//                break;
//            case ANIM_RIGHT_OUT_LEFT_IN:
//                activity.overridePendingTransition(R.anim.push_top_right_in,
//                        0);
//                break;
            default:
                break;
        }
    }

}
