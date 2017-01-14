package com.prgpr.framework.tasks;

/**
 * Created by kito on 13/01/17.
 */
public abstract class Task implements Runnable {
    public abstract String getDescription();
    public abstract String[] getRequirements();
    public abstract String[] produces();
}
