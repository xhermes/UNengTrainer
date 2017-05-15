package me.xeno.unengtrainer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/5/14.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            init();
    }

    protected abstract void setContentView();

    protected abstract void findViewById();

    protected void init() {
        setContentView();
        findViewById();
        initView();
        loadData();
    }

    protected abstract void initView();

    protected abstract void loadData();

    protected void showLoadingDialog() {
        //TODO
    }

    protected void hideLoadingDialog() {
        //TODO
    }

}
