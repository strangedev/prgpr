package com.prgpr.exceptions;

/**
 * Created by kito on 19.11.16.
 */
public class InvalidArgumentsException extends Exception
{
    public InvalidArgumentsException(){}

    public InvalidArgumentsException(String message)
    {
        super(message);
    }
}
