package com.prgpr.framework.threading;

/**
 * Created by kito on 11/17/16.
 */
public class ThreadMangerFactory {
    private static ThreadManager threadManager;
    public static ThreadManager getThreadManager(){
        if(threadManager != null){
            return threadManager;
        }
        threadManager = new ThreadManager();
        return threadManager;
    }
}
