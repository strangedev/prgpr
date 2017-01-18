package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.TaskFileArgument;
import com.prgpr.data.TaskDependencies;
import com.prgpr.exceptions.*;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskScheduler;
import com.prgpr.tasks.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Class to execute the tasks read from a task file.
 * @author Kyle Rinfreschi
 */
public class ExecuteTasksCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(ExecuteTasksCommand.class);

    private static final Task[] tasks = new Task[]{
            new CategoryLinkExtraction(),
            new EntityBaseExtraction(),
            new ArticleLinkExtraction(),
            new HTMLDumpImport(),
            new PersonExtraction(),
            new CityExtraction(),
            new MonumentExtraction()
    };

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new TaskFileArgument()
    };

    private Set<TaskDependencies> taskProducts = new LinkedHashSet<>();
    private Set<Task> requestedTasks = new LinkedHashSet<>();

    @Override
    public String getName() {
        return "executetasks";
    }

    @Override
    public String getDescription() {
        return "Runs all tasks from a task file.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public void handleArguments(List<String> args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.size() < 1){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args.get(0));
        arguments[1].set(args.get(1));
    }

    @Override
    public void run() {
        TaskScheduler scheduler = new TaskScheduler();

        try (Stream<String> stream = Files.lines(Paths.get(arguments[1].get()), StandardCharsets.UTF_8)) {
            stream.forEachOrdered(this::parseLine);
        } catch (IOException exception) {
            log.error("Couldn't get lines of file: " + arguments[1].get());
            System.exit(1);
        } catch (TaskNotFoundException | MissingDependencyException e) {
            log.catching(e);
            System.exit(1);
        }

        /*try {
            FileUtils.deleteRecursively(new File(arguments[0].get()));
        } catch (IOException e) {
            log.catching(e);
        }*/

        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());
        TaskScheduler.setDatabase(graphDb);

        scheduler.executeTasks(requestedTasks.toArray(new Task[requestedTasks.size()]));
    }

    /**
     * Reads the task from the line given from the file.
     * Adds to the tasks to execute.
     *
     * @param line with the task from the task file.
     * @throws TaskNotFoundException
     * @throws MissingDependencyException
     */
    private void parseLine(String line) throws TaskNotFoundException, MissingDependencyException {
        String[] lineParts = line.split("\\s+");
        Task current = Arrays.stream(tasks)
                .filter(task -> task.getClass().getSimpleName().toLowerCase()
                                               .equals(lineParts[0].toLowerCase()))
                .findFirst().orElse(null);

        if(current == null){
            throw new TaskNotFoundException(lineParts[0]);
        }

        if(current.getRequirements() != null) {
            checkDependencies(current);
        }

        String[] taskArgs = Arrays.copyOfRange(lineParts, 1, lineParts.length);
        current.setArguments(taskArgs);
        requestedTasks.add(current);
        taskProducts.addAll(Arrays.stream(current.produces()).collect(Collectors.toSet()));
    }

    /**
     * Checks if the task can be executed.
     *
     * @param current the task to check
     */
    private void checkDependencies(Task current) {
        Set<TaskDependencies> unfulfilledDependencies = Arrays.stream(current.getRequirements())
                .filter(req -> !taskProducts.contains(req))
                .collect(Collectors.toSet());

        if (unfulfilledDependencies.size() > 0) {
            //throw new MissingDependencyException();
        }
    }

}
