package org.example.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.repository.connection.DatabaseConnection;
import org.example.repository.connection.MongoConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class RezultatiSeeder {

    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        MongoDatabase db = MongoConnection.getInstance().getDatabase();
        MongoCollection<Document> kolekcija = db.getCollection("rezultati_eksperimenta");

        kolekcija.drop();

        String sql = "SELECT eksperiment_id, naziv, cilj FROM eksperiment WHERE status = 'Zavrsen'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("eksperiment_id");
                String naziv = rs.getString("naziv");
                String cilj = rs.getString("cilj");

                List<Document> merenja = Arrays.asList(
                        new Document("parametar", "vreme reakcije").append("vrednost", 310 + id * 7).append("jedinica", "ms"),
                        new Document("parametar", "tacnost").append("vrednost", 75 + id % 20).append("jedinica", "%"),
                        new Document("parametar", "broj pokusaja").append("vrednost", 5 + id % 3).append("jedinica", "kom")
                );

                Document doc = new Document("eksperiment_id", id)
                        .append("naziv", naziv)
                        .append("cilj", cilj)
                        .append("tip_rezultata", id % 2 == 0 ? "kvantitativni" : "kvalitativni")
                        .append("merenja", merenja)
                        .append("napomena", "Eksperiment uspešno završen. Rezultati u skladu sa očekivanjima.")
                        .append("datum_unosa", java.time.LocalDate.now().toString());

                kolekcija.insertOne(doc);
                System.out.println("Ubacen eksperiment ID: " + id + " – " + naziv);
            }
        }

        System.out.println("Seeding završen.");
    }
}