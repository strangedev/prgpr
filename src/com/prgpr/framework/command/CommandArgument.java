package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgument;

import java.util.Objects;

/**
 * Created by kito on 20.11.16.
 */
public abstract class CommandArgument {
    public String value;

    public abstract String getName();

    public abstract String getDescription();

    public abstract void test(String arg) throws InvalidArgument;

    public void set(String arg) throws InvalidArgument {
        this.test(arg);
        this.value = arg;
    }

    public String get() {
        return value;
    }

    @Override
    public int hashCode(){
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || getClass() ==  o.getClass();
    }
}
