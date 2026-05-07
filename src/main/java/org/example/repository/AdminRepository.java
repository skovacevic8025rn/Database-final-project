package org.example.repository;

import org.example.repository.connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public List<Object[]> findZakazaneSesije() throws SQLException {
        String sql =
                "SELECT s.sesija_id, s.datum, s.vreme_pocetka, s.vreme_zavrsetka, s.tip, " +
                        "e.naziv AS eksperiment, l.naziv AS laboratorija, iz.status AS status_izvodjenja " +
                        "FROM sesija s " +
                        "JOIN izvodjenje iz ON s.izvodjenje_id = iz.izvodjenje_id " +
                        "JOIN eksperiment e ON iz.eksperiment_id = e.eksperiment_id " +
                        "JOIN laboratorija l ON iz.laboratorija_id = l.laboratorija_id";
        List<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("sesija_id"),
                        rs.getString("datum"),
                        rs.getString("vreme_pocetka"),
                        rs.getString("vreme_zavrsetka"),
                        rs.getString("tip"),
                        rs.getString("eksperiment"),
                        rs.getString("laboratorija"),
                        rs.getString("status_izvodjenja")
                });
            }
        }
        return lista;
    }

    public boolean updateSesija(int sesijaId, String datum, String vremePocetka,
                                String vremeZavrsetka, String tip) throws SQLException {
        String sql = "UPDATE sesija SET datum = ?, vreme_pocetka = ?, vreme_zavrsetka = ?, tip = ? " +
                "WHERE sesija_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, datum);
            ps.setString(2, vremePocetka);
            ps.setString(3, vremeZavrsetka);
            ps.setString(4, tip);
            ps.setInt(5, sesijaId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Object[]> findSveLaboratorije() throws SQLException {
        String sql = "SELECT laboratorija_id, naziv FROM laboratorija ORDER BY naziv";
        List<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("laboratorija_id"),
                        rs.getString("naziv")
                });
            }
        }
        return lista;
    }

    public boolean mozeBrisatiLaboratoriju(int labId) throws SQLException {
        String sql =
                "SELECT COUNT(*) FROM eksperiment_istrazivac ei " +
                        "JOIN eksperiment e ON ei.eksperiment_id = e.eksperiment_id " +
                        "JOIN izvodjenje iz ON e.eksperiment_id = iz.eksperiment_id " +
                        "WHERE iz.laboratorija_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, labId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        }
        return false;
    }

    public boolean deleteLaboratorija(int labId) throws SQLException {
        if (!mozeBrisatiLaboratoriju(labId)) return false;
        String sql = "DELETE FROM laboratorija WHERE laboratorija_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, labId);
            return ps.executeUpdate() > 0;
        }
    }
}