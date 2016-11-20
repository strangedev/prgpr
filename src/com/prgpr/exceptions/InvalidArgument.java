package com.prgpr.exceptions;

/**
 * Created by kito on 20.11.16.
 */
public class InvalidArgument extends Exception {
    public InvalidArgument(){}

    public InvalidArgument(String message)
    {
        super(message);
    }
}
