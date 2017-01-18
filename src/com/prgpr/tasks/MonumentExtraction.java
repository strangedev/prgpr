package com.prgpr.tasks;

import com.prgpr.EntityFinder;
import com.prgpr.data.TaskDependencies;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskProgressLogger;

/**
 * Created by strange on 1/18/17.
 */
public class MonumentExtraction extends Task {

    @Override
    public String getDescription() {
        return "Extracts metadata for Monument entities";
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
                TaskDependencies.MonumentMetadata
        };
    }

    @Override
    public void run() {
        EntityFinder.setDatabase(this.db);
        this.subscribe(new TaskProgressLogger(true));
        EntityFinder.getAllMonuments().forEach(monument -> {
            monument.extractMetadata();
            this.emit(1);
        });
    }
}

