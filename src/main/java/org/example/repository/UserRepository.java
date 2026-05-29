package org.example.repository;

import org.example.model.Istrazivac;
import org.example.model.User;
import org.example.repository.connection.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public Optional<Istrazivac> findByKorisnickoImeILozinka(String korisnickoIme, String lozinka) throws SQLException {
        String sql = "SELECT k.korisnik_id, k.email, k.korisnicko_ime, k.lozinka, " +
                "    i.istrazivac_id, i.ime, i.prezime, i.kontakt " +
                "FROM korisnik k " +
                "JOIN istrazivac i ON k.istrazivac_id = i.istrazivac_id " +
                "WHERE k.korisnicko_ime = ? AND k.lozinka = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, korisnickoIme);
            ps.setString(2, lozinka);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Istrazivac ist = new Istrazivac();
                ist.setId(rs.getInt("korisnik_id"));
                ist.setEmail(rs.getString("email"));
                ist.setUsername(rs.getString("korisnicko_ime"));
                ist.setPassword(rs.getString("lozinka"));
                ist.setIstrazivacId(rs.getInt("istrazivac_id"));
                ist.setName(rs.getString("ime"));
                ist.setSurname(rs.getString("prezime"));
                ist.setKontakt(rs.getString("kontakt"));
                return Optional.of(ist);
            }
        }
        return Optional.empty();
    }

    public boolean existsByKorisnickoIme(String korisnickoIme) throws SQLException {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE korisnicko_ime = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, korisnickoIme);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE email = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean save(User user, int istrazivacId) throws SQLException {
        String sql = "INSERT INTO korisnik (email, korisnicko_ime, lozinka, istrazivac_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setInt(4, istrazivacId);
            return ps.executeUpdate() > 0;
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("korisnik_id"));
        u.setName(rs.getString("ime"));
        u.setSurname(rs.getString("prezime"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("korisnicko_ime"));
        u.setPassword(rs.getString("lozinka"));
        return u;
    }

    public boolean existsByIstrazivacId(int istrazivacId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE istrazivac_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, istrazivacId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }
}
