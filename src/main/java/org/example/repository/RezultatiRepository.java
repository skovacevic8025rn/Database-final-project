package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.repository.connection.MongoConnection;

import java.util.ArrayList;
import java.util.List;

public class RezultatiRepository {

    private MongoCollection<Document> getKolekcija() {
        MongoDatabase db = MongoConnection.getInstance().getDatabase();
        return db.getCollection("rezultati_eksperimenta");
    }

    public List<Document> findAll() {
        List<Document> lista = new ArrayList<>();
        for (Document d : getKolekcija().find()) {
            lista.add(d);
        }
        return lista;
    }

    public Document findByEksperimentId(int eksperimentId) {
        return getKolekcija().find(new Document("eksperiment_id", eksperimentId)).first();
    }
}