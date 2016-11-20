package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgument;

/**
 * Created by kito on 20.11.16.
 */
public abstract class CommandArgument {
    public String value;

    public abstract void test(String arg) throws InvalidArgument;

    public void set(String arg) throws InvalidArgument {
        this.test(arg);
        this.value = arg;
    }

    public String get() {
        return value;
    }
}
