package com.prgpr.framework.database;

/**
 * @author Noah Hummel
 * Created by strange on 11/20/16.
 *
 * An interface for a Callable with no return type, and variable argument type.
 * Can be used to pass function references and guarantee type safety on the argument type.
 */
public interface Callback<T> {

    /**
     * Call the callback (Run the method body)
     * @param arg The argument passed to the callback
     */
    void call(T arg);
}
