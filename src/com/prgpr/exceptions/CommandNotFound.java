package com.prgpr.exceptions;

/**
 * Created by kito on 19.11.16.
 */
public class CommandNotFound extends Exception
{
    public CommandNotFound(){}

    public CommandNotFound(String message)
    {
        super(message);
    }
}
