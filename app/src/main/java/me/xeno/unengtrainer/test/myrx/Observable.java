package me.xeno.unengtrainer.test.myrx;

import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2017/8/22.
 *
 */

public class Observable<T> {

    private OnSubscribe<T> mOnsubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        mOnsubscribe = onSubscribe;
    }

    /**
     * 这里是一个静态方法，声明了一个泛型N，并接受一个N类型的OnSubscribe来初始化Observable，
     * Observable.create(new OnSubscribe<T>())
     * create就是一个带onSubscribe的new的操作
     * @param onSubscribe 传入持有一个onSubscribe引用，OnSubscribe需要重写call()方法，
     *                    列出事件源中应该执行的任务
     * @return
     */
    public static <N> Observable<N> create(OnSubscribe<N> onSubscribe) {
        Logger.info("create");
        return new Observable<>(onSubscribe);
    }

    /**
     * 让本Observable和传入的Observer产生订阅关系
     * @param subscriber
     * @return
     */
    public void subscribe(Subscriber subscriber) {
        //生产了订阅关系以后，首先执行call()中的事件源的代码
        Logger.info("subscribe");
        mOnsubscribe.call(subscriber);
    }

    public <R> Observable<R> map(final Function<T,R> function) {
        Logger.info("map");
        OnSubscribe<R> onSubscribe2 = new OnSubscribe<R>() {
            @Override
            public void call(final Subscriber<R> subscriber) {
                Logger.info("call:(onSubscribe2)");
                Observable.this.subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T o) {
                        subscriber.onNext(function.fun(o));
                    }

                });
            }
        };

       Observable<R> observable2 = create(onSubscribe2);

        return observable2;
    }

    //OnSubscribe作为Observable的内部类，每个Observable持有一个引用，并且在创建时必须传入一个。
    public interface OnSubscribe<T> {


        /**
         * 在创建OnSubscribe的时候必须实现call()，
         * call()在subscribe()时会被调用，call()内部的代码是实际将被观察的事件源。
         * @param subscriber 方法将会携带一个参数subscriber，来自
         */
         void call(Subscriber<T> subscriber);
    }
}
