package com.prgpr.framework.command;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 20.11.16.
 *
 * Singleton factory for CommandBroker.
 * Makes sure no more than one CommandBroker is ever created.
 */
public class CommandBrokerFactory {

    private static CommandBroker commandBroker;

    /**
     * @return The singleton CommandBroker instance.
     */
    public static CommandBroker getCommandBroker(){
        if(commandBroker == null){
            commandBroker = new CommandBroker();
        }

        return commandBroker;
    }
}
