package me.xeno.unengtrainer.test;

import me.xeno.unengtrainer.test.myrx.Observable;
import me.xeno.unengtrainer.test.myrx.Observer;
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
        test();
    }

    @Override
    protected void loadData() {

    }

    public void createSource() {
        mOnSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<String> subscriber) {
                subscriber.onNext("123");
                subscriber.onCompleted();
            }
        };
    }

    public void test() {
        Observable.create(mOnSubscribe)
                .subscribe(new Subscriber() {
                    @Override
                    public void onSubscribe() {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
}
