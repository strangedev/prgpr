package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.data.Page;
import com.prgpr.data.TaskDependencies;
import com.prgpr.framework.tasks.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Kyle Rinfreschi
 */
public class ArticleLinkExtraction extends Task {
    private static final Logger log = LogManager.getFormatterLogger(ArticleLinkExtraction.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    @Override
    public String getDescription() {
        return "Inserts the links of the articles.";
    }

    @Override
    public TaskDependencies[] getRequirements() {
        return new TaskDependencies[]{
                TaskDependencies.Pages
        };
    }

    @Override
    public TaskDependencies[] produces() {
        return new TaskDependencies[]{
                TaskDependencies.ArticleLinks
        };
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run() {
        PageFactory.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);

        try {
            db.transaction(() -> {
                db.getAllElements()
                        .map(Page::new)
                        .forEach(Page::insertArticleLinks);
                return null;
            });
        } catch (Exception e) {
            log.catching(e);
        }
    }
}
