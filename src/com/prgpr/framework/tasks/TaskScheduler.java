package com.prgpr.framework.tasks;

import com.prgpr.exceptions.CircularDependencyException;
import com.prgpr.exceptions.MissingDependencyException;

import java.util.*;

/**
 * Created by kito on 13/01/17.
 */
public class TaskScheduler implements Runnable {
    private Set<Task> registeredTasks = new LinkedHashSet<>();
    private HashMap<String, Set<Task>> productsArray = new HashMap<>();

    /**
     * Adds a new command to the set of known tasks.
     *
     * @param task An instance of a new task to be added.
     * @return boolean
     */
    public boolean register(Task task){
        boolean success =  registeredTasks.add(task);

        if(!success)
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
    public void register(Task[] tasks){
        Arrays.stream(tasks).forEach(this::register);
    }

    public void run(){
        Task[] sortedTasks = new Task[0];

        try {
            sortedTasks = getTopologicallySortedTasks();
        } catch (CircularDependencyException | MissingDependencyException e) {
            e.printStackTrace();
        }

        Arrays.stream(sortedTasks)
                .forEach(Task::run);
    }

    private Task[] getTopologicallySortedTasks() {
        // initialization of graph representation
        HashMap<Task, Set<Task>> parentArray = new HashMap<>();

        // fill graph representation with default values
        registeredTasks.forEach(task -> {
            parentArray.put(task, new LinkedHashSet<>());
        });

        // update graph representation
        registeredTasks.forEach((task) ->
                Arrays.stream(task.getRequirements())
                        .flatMap((req) -> {
                            Set<Task> providers = productsArray.getOrDefault(req, new LinkedHashSet<>());

                            if(providers.isEmpty()){
                                throw new MissingDependencyException();
                            }

                            return providers.stream();
                        })
                        .forEach((req) -> parentArray.get(req).add(task))
        );

        Deque<Task> sortedTasks = new ArrayDeque<>();

        Set<Task> seenTasks = new HashSet<>();

        while (seenTasks.size() < registeredTasks.size()){
            for(Task task : registeredTasks) {

                Stack<Task> lifo = new Stack<>();
                Stack<Task> currentPath = new Stack<>();

                lifo.push(task);

                while (!lifo.empty()) {
                    Task current = lifo.pop();

                    if (currentPath.contains(current)) {
                        throw new CircularDependencyException();
                    }

                    // task not seen yet
                    if(seenTasks.contains(current))
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
