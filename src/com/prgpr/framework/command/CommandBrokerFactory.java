package com.prgpr.framework.command;

/**
 * Created by kito on 20.11.16.
 */
public class CommandBrokerFactory {
    private static CommandBroker commandBroker;

    public static CommandBroker getCommandBroker(){
        if(commandBroker == null){
            commandBroker = new CommandBroker();
        }

        return commandBroker;
    }
}
