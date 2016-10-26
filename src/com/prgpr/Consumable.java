package com.prgpr;

/**
 * Created by strange on 10/26/16.
 */
public class Consumable<T> {

    private T data;

    public Consumable(T data) {

        this.data = data;

    }

    public T get() {

        return this.data;

    }

}
