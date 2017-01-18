package com.prgpr.framework.tasks;

import com.prgpr.data.TaskDependencies;
import com.prgpr.exceptions.CircularDependencyException;
import com.prgpr.exceptions.MissingDependencyException;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.helpers.Benchmark;
import com.prgpr.tasks.HTMLDumpImport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author Kyle Rinfreschi
 */
public class TaskScheduler implements Runnable {
    private static final Logger log = LogManager.getFormatterLogger(TaskScheduler.class);
    private Set<Task> registeredTasks = new LinkedHashSet<>();
    private HashMap<TaskDependencies, Set<Task>> productsArray = new HashMap<>();

    private static EmbeddedDatabase db;

    /**
     * A function to set the database as the used by this instance.
     *
     * @param graphDb An embedded database which is used at runtime
     */
    public static void setDatabase(EmbeddedDatabase graphDb) {
        TaskScheduler.db = graphDb;
    }

    /**
     * Adds a new command to the set of known tasks.
     *
     * @param task An instance of a new task to be added.
     * @return boolean
     */
    public boolean register(Task task) {
        boolean success = registeredTasks.add(task);

        if (!success)
            return false;

        Arrays.stream(task.produces())
                .forEach((product) -> {
                    Set<Task> tasks = productsArray.getOrDefault(product, new LinkedHashSet<>());
                    tasks.add(task);
                    productsArray.put(product, tasks);
                });

        return true;
    }

    /**
     * Adds an array of new tasks to the set of known commands.
     *
     * @param tasks A list of instances of new tasks to be added.
     */
    public void register(Task[] tasks) {
        Arrays.stream(tasks).forEach(this::register);
    }

    /**
     * Try and sort all registered tasks in order to fulfill all their dependencies
     * during execution and execute them.
     */
    public void run() {
        Task[] sortedTasks;

        try {
            sortedTasks = getTopologicallySortedTasks();
            executeTasks(sortedTasks);
        } catch (CircularDependencyException | MissingDependencyException e) {
            log.catching(e);
        }
    }

    /**
     * Execute a list of tasks.
     *
     * @param tasks a list of tasks to execute.
     */
    public void executeTasks(Task[] tasks) {
        final long[] totalTimeTaken = {0};
        Arrays.stream(tasks)
                .forEach((task) -> {
                    String taskName = task.getClass().getSimpleName();
                    log.info(task.getClass().getSimpleName());

                    task.setDatabase(db);

                    long timeImport = Benchmark.run(task);
                    db.getTransactionManager().closeOpenTransactions();

                    totalTimeTaken[0] += timeImport;
                    log.info("(" + taskName + ") Time taken: " + (timeImport / 1000) + " seconds");
                });

        log.info("Total time taken: " + (totalTimeTaken[0] / 1000) + " seconds");
    }

    /**
     * Sort all registered tasks by their dependencies using topological sorting.
     * https://en.wikipedia.org/wiki/Topological_sorting
     *
     * @return a list of sorted tasks
     */
    private Task[] getTopologicallySortedTasks() {
        // initialization of graph representation
        HashMap<Task, Set<Task>> parentArray = new HashMap<>();

        // fill graph representation with default values
        registeredTasks.forEach(task -> {
            parentArray.put(task, new LinkedHashSet<>());
        });

        // update graph representation
        registeredTasks.forEach((task) -> {
                    if(task.getRequirements() == null)
                        return;

                    Arrays.stream(task.getRequirements())
                            .flatMap((req) -> {
                                Set<Task> providers = productsArray.getOrDefault(req, new LinkedHashSet<>());

                                if (providers.isEmpty()) {
                                    throw new MissingDependencyException();
                                }

                                return providers.stream();
                            })
                            .forEach((req) -> parentArray.get(req).add(task));
                }
        );

        Deque<Task> sortedTasks = new ArrayDeque<>();
        Set<Task> seenTasks = new HashSet<>();

        while (seenTasks.size() < registeredTasks.size()) {
            for (Task task : registeredTasks) {

                Stack<Task> lifo = new Stack<>();
                Stack<Task> currentPath = new Stack<>();

                lifo.push(task);

                while (!lifo.empty()) {
                    Task current = lifo.pop();

                    if (currentPath.contains(current)) {
                        throw new CircularDependencyException();
                    }

                    // task not seen yet
                    if (seenTasks.contains(current))
                        continue;

                    currentPath.push(current);

                    // add children to stack
                    parentArray.get(current)
                            .forEach(lifo::push);
                }

                while (!currentPath.empty()) {
                    Task t = currentPath.pop();
                    seenTasks.add(t);
                    sortedTasks.addFirst(t);
                }

                // unflag and remove from traversal
                currentPath.clear();
            }
        }

        return sortedTasks.toArray(new Task[sortedTasks.size()]);
    }
}
