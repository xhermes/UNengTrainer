package me.xeno.unengtrainer.test.myrx;

/**
 * Created by xeno on 2017/8/22.
 */

public interface Observer<T> {
    void onSubscribe();
    void onNext(T t);
    void onCompleted();
    void onError(Exception e);
}
