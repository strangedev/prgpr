package com.prgpr.framework.command;

/**
 * Created by kito on 19.11.16.
 */
public interface Command {

    String getName();

    int execute(String[] args);
}
