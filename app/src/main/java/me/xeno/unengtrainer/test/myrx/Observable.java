package me.xeno.unengtrainer.test.myrx;

/**
 * Created by xeno on 2017/8/22.
 */

public class Observable {
    private OnSubscribe mOnsubscribe;

    private Observable(OnSubscribe onSubscribe) {
        mOnsubscribe = onSubscribe;
    }

    public static Observable create(OnSubscribe onSubscribe) {
        return new Observable(onSubscribe);
    }

    /**
     * 让本Observable和传入的Observer产生订阅关系
     * @param observer
     * @return
     */
    public Disposable subscribe(Observer observer) {
        //生产了订阅关系以后，首先执行call()中的事件源的代码
        mOnsubscribe.call();
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
