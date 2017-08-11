package me.xeno.unengtrainer.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2017/8/9.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        test();
    }

    public void test() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Logger.info( "create " + Thread.currentThread().getName());
                e.onNext(1);
            }
        }).map(new Function<Integer, Integer>() {

            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                Logger.info( "map " + Thread.currentThread().getName());
                return 1;
            }
        })
                .subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Integer>() {

                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        Logger.info( "map2 " + Thread.currentThread().getName());
                        return 1;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Logger.info( "onSubscribe " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Logger.info( "onNext " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.info(Thread.currentThread() + "");
                    }

                    @Override
                    public void onComplete() {
                        Logger.info( "onComplete " + Thread.currentThread().getName());
                    }
                });
    }
}
