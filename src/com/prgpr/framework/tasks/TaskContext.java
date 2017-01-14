package com.prgpr.framework.tasks;

import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;

/**
 * Created by kito on 14/01/17.
 */
public class TaskContext {
    private final EmbeddedDatabase db;
    private final CommandArgument[] args;

    TaskContext(EmbeddedDatabase db, CommandArgument[] args){
        this.db = db;
        this.args = args;
    }

    public CommandArgument[] getArgs() {
        return args;
    }

    public EmbeddedDatabase getDb() {
        return db;
    }
}
