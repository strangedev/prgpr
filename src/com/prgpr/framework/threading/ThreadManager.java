package com.prgpr.framework.threading;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kito on 11/17/16.
 */
public class ThreadManager {
    private HashMap<ThreadType, ExecutorService> executorServices;
    private static final ThreadType defaultThread = ThreadType.IO;

    ThreadManager(){
        executorServices = new HashMap<ThreadType, ExecutorService>(){{
            put(ThreadType.COMPUTATIONAL, Executors.newFixedThreadPool(3, new ComputationThreadFactory()));
            put(ThreadType.IO, Executors.newFixedThreadPool(1, new IOThreadFactory()));
        }};
    }

    public void execute(ThreadType thread, Runnable task){
        ExecutorService service = executorServices.getOrDefault(thread, executorServices.get(defaultThread));
        service.execute(task);
    }

    public void execute(Runnable task){
        ExecutorService service = executorServices.get(defaultThread);
        service.execute(task);
    }
}
