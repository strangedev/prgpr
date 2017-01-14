package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.data.Page;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.transaction.BatchTransactionManager;
import com.prgpr.framework.database.transaction.TransactionManager;
import com.prgpr.framework.tasks.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.kernel.api.impl.schema.verification.DuplicateCheckingCollector;

/**
 * Created by kito on 13/01/17.
 */
public class ArticleLinkExtraction extends Task {
    private static final Logger log = LogManager.getFormatterLogger(ArticleLinkExtraction.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    @Override
    public String getDescription() {
        return "Inserts the links of the articles.";
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
                "ArticleLinks"
        };
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run() {
        EmbeddedDatabase db = this.context.getDb();

        PageFactory.setDatabase(db);

        TransactionManager tm = new BatchTransactionManager(db.getTransactionManager(), batchSize);
        db.setTransactionManager(tm);

        try {
            db.transaction(() -> db.getAllElements()
                    .map(Page::new)
                    .forEach(Page::insertArticleLinks));
        } catch (Exception e) {
            log.catching(e);
        }
    }
}
