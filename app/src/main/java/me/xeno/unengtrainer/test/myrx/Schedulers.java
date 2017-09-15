package me.xeno.unengtrainer.test.myrx;

import java.util.concurrent.Executors;

/**
 * Created by xeno on 2017/8/29.
 */

public class Schedulers {
    //交给了java的线程池处理该Runnable
    private static final Scheduler ioScheduler = new Scheduler(Executors.newSingleThreadExecutor());
    private static final Scheduler ioScheduler2 = new Scheduler(Executors.newCachedThreadPool());
    public static Scheduler io() {
        return ioScheduler;
    }
    public static Scheduler io2() {
        return ioScheduler2;
    }
}
