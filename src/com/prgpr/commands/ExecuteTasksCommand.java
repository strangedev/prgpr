package com.prgpr.commands;

import com.prgpr.commands.arguments.TaskFileArgument;
import com.prgpr.exceptions.*;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskScheduler;
import com.prgpr.tasks.ArticleLinkExtraction;
import com.prgpr.tasks.CategoryLinkExtraction;
import com.prgpr.tasks.EntityBaseExtraction;
import com.prgpr.tasks.HTMLDumpImport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lissie on 1/10/17.
 */
public class ExecuteTasksCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(ExecuteTasksCommand.class);

    private static final Task[] tasks = new Task[]{
            new CategoryLinkExtraction(),
            new EntityBaseExtraction(),
            new ArticleLinkExtraction(),
            new HTMLDumpImport(),
    };

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new TaskFileArgument()
    };

    private Set<String> taskProducts = new LinkedHashSet<>();
    private Set<Task> requestedTasks = new LinkedHashSet<>();

    @Override
    public String getName() {
        return "executetasks";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.length < 1){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args[0]);
    }

    @Override
    public void run() {
        TaskScheduler scheduler = new TaskScheduler();

        try (Stream<String> stream = Files.lines(Paths.get(arguments[0].get()), StandardCharsets.UTF_8)) {
            stream.forEachOrdered(this::parseLine);
        } catch (IOException exception) {
            log.error("Couldn't get lines of file: " + arguments[0].get());
            System.exit(1);
        } catch (TaskNotFoundException | MissingDependencyException e) {
            log.catching(e);
            System.exit(1);
        }

        scheduler.executeTasks(requestedTasks.toArray(new Task[requestedTasks.size()]));
    }

    private void parseLine(String line) throws TaskNotFoundException, MissingDependencyException {
        String[] lineParts = line.split("\\s+");
        Task current = Arrays.stream(tasks)
                .filter(task -> task.getClass().getSimpleName().equals(lineParts[0]))
                .findFirst().orElse(null);

        if(current == null){
            throw new TaskNotFoundException(lineParts[0]);
        }

        Set<String> unfufilledDependencies = Arrays.stream(current.getRequirements())
                .filter(req -> !taskProducts.contains(req))
                .collect(Collectors.toSet());

        if(unfufilledDependencies.size() > 0){
            throw new MissingDependencyException();
        }

        String[] taskArgs = Arrays.copyOfRange(lineParts, 1, lineParts.length);
        current.setArguments(taskArgs);
        requestedTasks.add(current);
        taskProducts.addAll(Arrays.stream(current.produces()).collect(Collectors.toSet()));
    }

}
