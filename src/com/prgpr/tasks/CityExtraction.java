package com.prgpr.tasks;

import com.prgpr.EntityFinder;
import com.prgpr.data.TaskDependencies;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskProgressLogger;

/**
 * Created by strange on 1/18/17.
 */
public class CityExtraction extends Task {

    @Override
    public String getDescription() {
        return "Extracts metadata for City entities";
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
                TaskDependencies.CityMetadata
        };
    }

    @Override
    public void run() {
        EntityFinder.setDatabase(this.db);
        this.subscribe(new TaskProgressLogger(true));
        EntityFinder.getAllCities().forEach(city -> {
            city.extractMetadata();
            this.emit(1);
        });
    }
}
