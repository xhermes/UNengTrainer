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

    public void test() {
        Observable.create(new Observable.OnSubscribe() {

            @Override
            public void call(Subscriber subscriber) {
                subscriber.onNext();
                subscriber.onCompleted();
            }
        }).subscribe(new Observer() {
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
