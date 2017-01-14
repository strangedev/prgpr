package com.prgpr.framework.tasks;

/**
 * Created by kito on 13/01/17.
 */
public abstract class Task implements Runnable {
    protected TaskContext context;

    public void setContext(TaskContext context){
        this.context = context;
    }

    public abstract String getDescription();
    public abstract String[] getRequirements();
    public abstract String[] produces();
}
