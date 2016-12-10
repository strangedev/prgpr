package com.prgpr.framework.command;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 20.11.16.
 *
 * Singleton factory for CommandBroker.
 */
public class CommandBrokerFactory {
    private static CommandBroker commandBroker;

    /**
     * Instantiates the CommandBroker class.
     * @return an instance of the CommandBroker
     */
    public static CommandBroker getCommandBroker(){
        if(commandBroker == null){
            commandBroker = new CommandBroker();
        }

        return commandBroker;
    }
}
