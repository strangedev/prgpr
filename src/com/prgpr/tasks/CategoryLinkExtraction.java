package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.data.Page;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.transaction.DefaultTransactionManager;
import com.prgpr.framework.tasks.Task;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by kito on 13/01/17.
 */
public class CategoryLinkExtraction extends Task {
    private static final Logger log = LogManager.getFormatterLogger(CategoryLinkExtraction.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    @Override
    public String getDescription() {
        return "Inserts the links of the categories.";
    }

    @Override
    public String[] getRequirements() {
        return new String[]{
                "Pages"
        };
    }

    @Override
    public String[] produces() {
        return new String[]{
                "CategoryLinks"
        };
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run() {
        EmbeddedDatabase db = this.context.getDb();

        PageFactory.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);

        try {
            db.transaction(() -> {
                db.getAllElements()
                        .map(Page::new)
                        .forEach(Page::insertCategoryLinks);
                return null;
            });
        } catch (Exception e) {
            log.catching(e);
        }
    }
}