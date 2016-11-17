package com.prgpr.framework.threading;

import java.util.concurrent.ThreadFactory;

class ComputationThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, ThreadType.COMPUTATIONAL.name());
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
