package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.repository.connection.DatabaseConnection;
import org.example.repository.connection.MongoConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SesijaRepository {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public List<Object[]> findSesijeByIstrazivac(int istrazivacId) throws SQLException {
        String sql =
                "SELECT s.sesija_id, s.datum, s.vreme_pocetka, s.vreme_zavrsetka, s.tip, e.naziv AS eksperiment " +
                        "FROM sesija s " +
                        "JOIN izvodjenje i ON s.izvodjenje_id = i.izvodjenje_id " +
                        "JOIN eksperiment e ON i.eksperiment_id = e.eksperiment_id " +
                        "JOIN eksperiment_istrazivac ei ON e.eksperiment_id = ei.eksperiment_id " +
                        "WHERE ei.istrazivac_id = ?";
        List<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, istrazivacId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("sesija_id"),
                        rs.getString("datum"),
                        rs.getString("vreme_pocetka"),
                        rs.getString("vreme_zavrsetka"),
                        rs.getString("tip"),
                        rs.getString("eksperiment")
                });
            }
        }
        return lista;
    }

    public boolean mozeBrisatiSesiju(int sesijId, int istrazivacId) throws SQLException {
        String sql =
                "SELECT COUNT(*) FROM sesija s " +
                        "JOIN izvodjenje i ON s.izvodjenje_id = i.izvodjenje_id " +
                        "JOIN eksperiment_istrazivac ei ON i.eksperiment_id = ei.eksperiment_id " +
                        "WHERE s.sesija_id = ? AND ei.istrazivac_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, sesijId);
            ps.setInt(2, istrazivacId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean deleteSesija(int sesijId, int istrazivacId) throws SQLException {
        if (!mozeBrisatiSesiju(sesijId, istrazivacId)) return false;
        String sql = "DELETE FROM sesija WHERE sesija_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, sesijId);
            boolean obrisan = ps.executeUpdate() > 0;
            if (obrisan) {
                MongoDatabase db = MongoConnection.getInstance().getDatabase();
                MongoCollection<Document> log = db.getCollection("sesija_log");
                log.insertOne(new Document("sesija_id", sesijId)
                        .append("istrazivac_id", istrazivacId)
                        .append("akcija", "brisanje")
                        .append("vreme", new Date()));
            }
            return obrisan;
        }
    }
}