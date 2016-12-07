package com.prgpr.exceptions;

/**
 * @author Kyle Rinfreschi
 *
 * A Class to catch exceptions where the command was not found
 */
public class InvalidArgument extends Exception
{
    /**
     * A function to trow an exception.
     */
    public InvalidArgument(){}

    /**
     * A function to trow an exception with a message.
     */
    public InvalidArgument(String message)
    {
        super(message);
    }
}
