package org.example.repository;

import org.example.model.Istrazivac;
import org.example.repository.connection.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IstrazivacRepository {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
    // Vraca istrazivaca na osnovu imena i prezimena.
    public Optional<Istrazivac> findByIme(String ime, String prezime) throws SQLException {
        String sql = "SELECT * FROM istrazivac WHERE ime = ? AND prezime = ? LIMIT 1";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Istrazivac i = new Istrazivac();
                i.setIstrazivacId(rs.getInt("istrazivac_id"));
                i.setName(rs.getString("ime"));
                i.setSurname(rs.getString("prezime"));

                i.setKontakt(rs.getString("kontakt"));
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
    // Prikazuje eksperimente na kojima istrazivac ucestvuje.
    public List<Object[]> findPlaniraniZavrseniEksperimenti(int istrazivacId) throws SQLException {
        String sql =
                "SELECT e.eksperiment_id, e.naziv, e.cilj, e.datum_pocetka, e.datum_zavrsetka, " +
                        "e.status AS oznaka " +
                        "FROM eksperiment e " +
                        "JOIN eksperiment_istrazivac ei ON e.eksperiment_id = ei.eksperiment_id " +
                        "WHERE ei.istrazivac_id = ?";
        List<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, istrazivacId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("eksperiment_id"),
                        rs.getString("naziv"),
                        rs.getString("cilj"),
                        rs.getString("datum_pocetka"),
                        rs.getString("datum_zavrsetka"),
                        rs.getString("oznaka")
                });
            }
        }
        return lista;
    }
    // Menja status eksperimenta na osnovu prosledjenog ID-ja.
    public boolean updateStatusEksperimenta(int eksperimentId, String noviStatus) throws SQLException {
        String sql = "UPDATE eksperiment SET status = ? WHERE eksperiment_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, noviStatus);
            ps.setInt(2, eksperimentId);
            return ps.executeUpdate() > 0;
        }
    }
}