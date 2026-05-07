package org.example.repository;

import org.example.repository.connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EksterniKorisnikRepository {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public List<Object[]> findLaboratorijeIIstrazivaci() throws SQLException {
        String sql =
                "SELECT DISTINCT l.naziv AS laboratorija, l.lokacija, " +
                        "i.ime, i.prezime, i.kontakt " +
                        "FROM laboratorija l " +
                        "JOIN izvodjenje iz ON l.laboratorija_id = iz.laboratorija_id " +
                        "JOIN eksperiment e ON iz.eksperiment_id = e.eksperiment_id " +
                        "JOIN eksperiment_istrazivac ei ON e.eksperiment_id = ei.eksperiment_id " +
                        "JOIN istrazivac i ON ei.istrazivac_id = i.istrazivac_id " +
                        "ORDER BY l.naziv, i.prezime";
        List<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("laboratorija"),
                        rs.getString("lokacija"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("kontakt")
                });
            }
        }
        return lista;
    }

    public boolean updateKorisnickoImeILozinka(int korisnikId, String novoKorisnickoIme,
                                               String novaLozinka) throws SQLException {
        String sql = "UPDATE korisnik SET korisnicko_ime = ?, lozinka = ? WHERE korisnik_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, novoKorisnickoIme);
            ps.setString(2, novaLozinka);
            ps.setInt(3, korisnikId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean proveriLozinku(int korisnikId, String lozinka) throws SQLException {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE korisnik_id = ? AND lozinka = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, korisnikId);
            ps.setString(2, lozinka);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean deleteNalog(int korisnikId, String lozinka) throws SQLException {
        if (!proveriLozinku(korisnikId, lozinka)) return false;
        String sql = "DELETE FROM korisnik WHERE korisnik_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, korisnikId);
            return ps.executeUpdate() > 0;
        }
    }
}