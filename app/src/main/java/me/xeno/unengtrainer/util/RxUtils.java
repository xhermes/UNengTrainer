package me.xeno.unengtrainer.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.ObservableRepeat;
import io.reactivex.internal.operators.observable.ObservableTimer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/21.
 */

public class RxUtils {

    public static Observable<Long> timer(int seconds) {
        return ObservableTimer.timer(seconds, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
