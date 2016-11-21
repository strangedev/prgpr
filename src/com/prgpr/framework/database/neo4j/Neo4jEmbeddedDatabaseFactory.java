package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.neo4j.TransactionManager;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by kito on 19.11.16.
 */
public class Neo4jEmbeddedDatabaseFactory {
    public static Neo4jEmbeddedDatabase newEmbeddedDatabase(String path){
        Neo4jEmbeddedDatabase db = new Neo4jEmbeddedDatabase();
        db.create(path);
        return db;
    }

    public static Neo4jEmbeddedDatabase newEmbeddedDatabase(GraphDatabaseService db){
        return new Neo4jEmbeddedDatabase(db);
    }
}
