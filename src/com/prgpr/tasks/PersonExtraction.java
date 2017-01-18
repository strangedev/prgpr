package com.prgpr.tasks;

import com.prgpr.EntityFinder;
import com.prgpr.data.Person;
import com.prgpr.data.TaskDependencies;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskProgressLogger;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by strange on 1/18/17.
 */
public class PersonExtraction extends Task {

    @Override
    public String getDescription() {
        return "Extracts metadata for Person entities";
    }

    @Override
    public TaskDependencies[] getRequirements() {
        return new TaskDependencies[]{
                TaskDependencies.Entities
        };
    }

    @Override
    public TaskDependencies[] produces() {
        return new TaskDependencies[]{
                TaskDependencies.PersonMetadata
        };
    }

    @Override
    public void run() {
        EntityFinder.setDatabase(this.db);
        this.subscribe(new TaskProgressLogger(true));

        Stream<Person> allPersons = EntityFinder.getAllPersons();
        long c = allPersons.count();

        EntityFinder.getAllPersons().forEach(person -> {
            person.extractMetadata();
            this.emit(1);
        });
    }
}
