package com.prgpr.framework.threading;

import java.util.concurrent.ThreadFactory;

public class IOThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, ThreadType.IO.name());
        t.setPriority(Thread.MAX_PRIORITY);
        return t;
    }
}
