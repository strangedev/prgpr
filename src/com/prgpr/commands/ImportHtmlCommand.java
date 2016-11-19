package com.prgpr.commands;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.data.Page;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.database.DatabaseFactory;
import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.ProducerLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * Created by kito on 19.11.16.
 */
public class ImportHtmlCommand implements Command {

    private static final Logger log = LogManager.getFormatterLogger(Command.class);

    @Override
    public String getName() {
        return "importhtml";
    }

    @Override
    public int execute(String[] args) {
        GraphDatabaseService graphDb = DatabaseFactory.newEmbeddedDatabase(args[0]);
        PageFactory.setDatabase(graphDb);

        PageProducer pageProducer = new PageProducer(args[1]);

        // Logging units
        ProducerLogger<Page> pageFactoryLogger = new ProducerLogger<>(true);
        pageFactoryLogger.subscribeTo(pageProducer);

        // Execute
        pageProducer.run();

        long time = Benchmark.run(()->{
            try ( Transaction tx = graphDb.beginTx() ) {
                log.info(graphDb.getAllNodes().stream().count());
            }
        });

        log.info("Counted nodes in: " + (time / 1000.0) + " s");

        return 0;
    }
}
