package com.prgpr.exceptions;

/**
 * Created by kito on 19.11.16.
 */
public class InvalidNumberOfArguments extends Exception
{
    public InvalidNumberOfArguments(){}

    public InvalidNumberOfArguments(String message)
    {
        super(message);
    }
}
