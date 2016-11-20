package com.prgpr.framework.database;

import java.util.concurrent.Callable;

/**
 * Created by strange on 11/20/16.
 */
public interface Callback<T> {

    void call(T args);
}
