package me.xeno.unengtrainer.test.myrx;

import java.util.concurrent.Executor;

/**
 * Created by xeno on 2017/8/29.
 */

public class Scheduler {
    private Executor executor;

    public Scheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new Worker(executor);
    }

    public static class Worker{
        private Executor executor;

        public Worker(Executor executor) {
            this.executor = executor;
        }

        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }
    }
}
