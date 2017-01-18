package com.prgpr.exceptions;

/**
 * Created by strange on 10/21/16.
 *
 * An Exception thrown when a malformed article was discovered.
 *
 * @author Noah Hummel
 */
public class MalformedWikidataException extends RuntimeException
{
    /**
     * A function to throw an exception.
     */
    public MalformedWikidataException(){}

    /**
     * A function to throw an exception with a message.
     * @param message the message to be displayed
     */
    public MalformedWikidataException(String message)
    {
        super(message);
    }
}