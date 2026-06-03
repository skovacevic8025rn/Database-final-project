package org.example.repository.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MongoConnection {

    private static final String URI;
    private static final String DB_NAME;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("mongo.cfg"));
            String host = props.getProperty("host").trim();
            String port = props.getProperty("port").trim();
            DB_NAME = props.getProperty("db").trim();
            URI = "mongodb://" + host + ":" + port;
        } catch (IOException e) {
            throw new RuntimeException("Ne mogu da učitam mongo.cfg fajl!", e);
        }
    }

    private static MongoConnection instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private MongoConnection() {
        this.mongoClient = MongoClients.create(URI);
        this.database = mongoClient.getDatabase(DB_NAME);
    }

    public static synchronized MongoConnection getInstance() {
        if (instance == null) {
            instance = new MongoConnection();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}