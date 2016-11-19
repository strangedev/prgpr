package com.prgpr.framework.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by kito on 19.11.16.
 */
public class DatabaseFactory {

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).

        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                TransactionManager.shutdown();
                graphDb.shutdown();
            }
        } );
    }

    public static GraphDatabaseService newEmbeddedDatabase(String filename){
        File dbf = new File(filename);

        /*
        // Just for testing reasons
        if (dbf.exists()) try {
            FileUtils.deleteRecursively(dbf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbf);
        registerShutdownHook(graphDb);
        return graphDb;
    }
}
