package com.prgpr.exceptions;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * Class of our own exeptions for Wikidatas
 */
public class MalformedWikidataException extends RuntimeException
{
    public MalformedWikidataException(){}

    public MalformedWikidataException(String message)
    {
        super(message);
    }
}