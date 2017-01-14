package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.PageFinder;
import com.prgpr.TypeExtraction;
import com.prgpr.data.*;
import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.framework.tasks.Task;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.prgpr.PageFinder.findAllByNamespace;

/**
 * Created by kito on 13/01/17.
 */
public class EntityBaseExtraction extends Task {
    private static final Logger log = LogManager.getFormatterLogger(EntityBaseExtraction.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    @Override
    public String getDescription() {
        return "Inserts the links of the articles.";
    }

    @Override
    public String[] getRequirements() {
        return new String[]{
                "CategoryLinks",
                "ArticleLinks"
        };
    }

    @Override
    public String[] produces() {
        return new String[]{
                "EntityLinks"
        };
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run() {
        EmbeddedDatabase db = this.context.getDb();

        PageFinder.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);

        try {
            Set<Page> pages = findAllByNamespace(0);
            Set<EntityBase> entities = new LinkedHashSet<>();
            for (Page page: pages) {
                Set<Page> entityTypes = TypeExtraction.discoverTypes(page);
                for (Page entity : entityTypes) {
                    switch (entity.getTitle()){
                        case "Person":
                            entities.add(new Person(db, page));
                            break;
                        case "Ort":
                            entities.add(new City(db, page));
                            break;
                        case "Denkmal":
                            entities.add(new Monument(db, page));
                            break;
                    }
                }
            }
            insertEntityBaseLinks(entities);
        } catch (Exception e){
            log.catching(e);
        }
    }

    private void insertEntityBaseLinks(Set<EntityBase> toAdd) {
        for (EntityBase adding : toAdd) {
            adding.insertEntityLinks();
        }
    }
}
