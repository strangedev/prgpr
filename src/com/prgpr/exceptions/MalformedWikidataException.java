package com.prgpr.exceptions;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * An Exception thrown when a malformed article was discovered
 */
public class MalformedWikidataException extends RuntimeException
{
    /**
     * A function to trow an exception.
     */
    public MalformedWikidataException(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public MalformedWikidataException(String message)
    {
        super(message);
    }
}