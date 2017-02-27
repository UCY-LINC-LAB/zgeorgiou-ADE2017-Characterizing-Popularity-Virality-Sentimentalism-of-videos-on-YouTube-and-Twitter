package com.zgeorg03.database;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class DBConnection {
    private final MongoClient client;
    private final MongoDatabase database;

    public DBConnection(String database,ServerAddress ...servers) {
        client =new MongoClient(Arrays.asList(servers));
        this.database = client.getDatabase(database);

    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
