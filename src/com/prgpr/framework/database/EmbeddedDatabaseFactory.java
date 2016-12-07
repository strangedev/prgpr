package com.prgpr.framework.database;

import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabase;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by kito on 19.11.16.
 */
public class EmbeddedDatabaseFactory {
    public static EmbeddedDatabase newEmbeddedDatabase(String path){
        Neo4jEmbeddedDatabase db = new Neo4jEmbeddedDatabase();
        db.create(path);
        return db;
    }

    public static EmbeddedDatabase newEmbeddedDatabase(GraphDatabaseService db){
        return new Neo4jEmbeddedDatabase(db);
    }
}
