package com.prgpr.framework.tasks;

import com.prgpr.data.TaskDependencies;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.consumer.Producer;
import com.prgpr.framework.database.EmbeddedDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * The Task interface.
 *
 * @author Kyle Rinfreschi
 */
public abstract class Task extends Producer<Integer> implements Runnable {
    protected CommandArgument[] arguments = new CommandArgument[0];
    protected EmbeddedDatabase db;

    /**
     * @return the description.
     */
    public abstract String getDescription();

    /**
     * @return A list of task dependencies.
     */
    public abstract TaskDependencies[] getRequirements();

    /**
     * @return A list of products to be depended on by other tasks.
     */
    public abstract TaskDependencies[] produces();

    /**
     * Checks and validates arguments to be used by the task.
     *          new CategoryLinkExtraction(),
            new EntityBaseExtraction(),
            new ArticleLinkExtraction(),
            new HTMLDumpImport(),
            new PersonExtraction(),
            new CityExtraction(),
            new MonumentExtraction()
     * @param arguments list of arguments to be used.
     * @throws InvalidNumberOfArguments An incorrect number of arguments are passed to the task.
     * @throws InvalidArgument An unexpected argument is passed.
     */
    protected void handleArguments(List<String> arguments) throws InvalidNumberOfArguments, InvalidArgument {}

    /**
     * Set a variable list of arguments.
     * @param arguments list of arguments to be used.
     * @throws InvalidNumberOfArguments An incorrect number of arguments are passed to the task.
     * @throws InvalidArgument An unexpected argument is passed.
     */
    public void setArguments(String... arguments) throws InvalidArgument, InvalidNumberOfArguments {
        handleArguments(Arrays.asList(arguments));
    }

    /**
     * Set the database to be used.
     * @param db a EmbeddedDatabase instance.
     */
    public void setDatabase(EmbeddedDatabase db) {
        this.db = db;
    }
}
