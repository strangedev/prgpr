package com.prgpr.exceptions;

/**
 * Created by kito on 19.11.16.
 */
public class CommandNotFoundException extends Exception
{
    public CommandNotFoundException(){}

    public CommandNotFoundException(String message)
    {
        super(message);
    }
}
