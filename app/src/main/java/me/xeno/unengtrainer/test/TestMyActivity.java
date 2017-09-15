package me.xeno.unengtrainer.test;

import me.xeno.unengtrainer.test.myrx.Function;
import me.xeno.unengtrainer.test.myrx.Observable;
import me.xeno.unengtrainer.test.myrx.Observer;
import me.xeno.unengtrainer.test.myrx.Schedulers;
import me.xeno.unengtrainer.test.myrx.Subscriber;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.view.activity.BaseActivity;

/**
 * Created by xeno on 2017/8/22.
 */

public class TestMyActivity extends BaseActivity {

    private Observable.OnSubscribe<String> mOnSubscribe;


    @Override
    protected void setContentView() {

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        createSource();
        test();
    }

    @Override
    protected void loadData() {

    }

    public void createSource() {

    }

    public void test() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<String> subscriber) {
                Logger.info("源的call层：" + Thread.currentThread().getName());
                subscriber.onNext("123");
            }
        })
                .map(new Function<String, Object>() {

                    @Override
                    public Object fun(String s) {
                        return "R";
                    }
                })
                .subscribeOn(Schedulers.io2())
                .observeOn(Schedulers.io())
                .map(new Function<Object, Object>() {

                    @Override
                    public Object fun(Object s) {
                        return "R";
                    }
                })
                .subscribe(new Subscriber() {

                    @Override
                    public void onNext(Object o) {
                        Logger.info("终端 onNext()" + Thread.currentThread().getName());
                    }

                });

    }
}
