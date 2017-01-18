package com.prgpr.tasks;

import com.prgpr.PageFinder;
import com.prgpr.TypeExtraction;
import com.prgpr.data.*;
import com.prgpr.framework.tasks.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.prgpr.PageFinder.findAllByNamespace;

/**
 * @author Kyle Rinfreschi
 */
public class EntityBaseExtraction extends Task {
    private static final Logger log = LogManager.getFormatterLogger(EntityBaseExtraction.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    @Override
    public String getDescription() {
        return "Inserts the links of the articles.";
    }

    @Override
    public TaskDependencies[] getRequirements() {
        return new TaskDependencies[]{
                TaskDependencies.CategoryLinks,
                TaskDependencies.ArticleLinks
        };
    }

    @Override
    public TaskDependencies[] produces() {
        return new TaskDependencies[]{
                TaskDependencies.Entities
        };
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run() {
        PageFinder.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);
        int count = 0; // FIXME remove

        try {
            Set<Page> pages = findAllByNamespace(0);
            Set<EntityBase> entities = new LinkedHashSet<>();
            for (Page page: pages) {
                Set<Page> entityTypes = TypeExtraction.discoverTypes(page);
                for (Page entity : entityTypes) {
                    switch (entity.getTitle()){
                        case "Person":
                            entities.add(new Person(db, page));
                            count++; // FIXME remove
                            break;
                        case "Ort":
                            entities.add(new City(db, page));
                            count++; // FIXME remove
                            break;
                        case "Denkmal":
                            entities.add(new Monument(db, page));
                            count++; // FIXME remove
                            break;
                    }
                }
                if (count > 300) break; // FIXME remove
            }
            insertEntityBaseLinks(entities);
        } catch (Exception e){
            log.catching(e);
        }
    }

    private void insertEntityBaseLinks(Set<EntityBase> toAdd) {
        for (EntityBase adding : toAdd) {
            adding.insertEntityLinks();
            adding.insertEntityId();
        }
    }
}
