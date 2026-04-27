package org.example.repository;


import org.example.model.User;
import org.example.repository.connection.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public Optional<User> findByKorisnickoImeILozinka(String korisnickoIme, String lozinka) throws SQLException {
        String sql = "SELECT * FROM users WHERE korisnicko_ime = ? AND lozinka = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, korisnickoIme);
            ps.setString(2, lozinka);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public boolean existsByKorisnickoIme(String korisnickoIme) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE korisnicko_ime = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, korisnickoIme);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean save(User user) throws SQLException {
        String sql = "INSERT INTO users (ime, prezime, email, korisnicko_ime, lozinka, uloga) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUsername());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());
            return ps.executeUpdate() > 0;
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("ime"));
        u.setSurname(rs.getString("prezime"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("korisnicko_ime"));
        u.setPassword(rs.getString("lozinka"));
        u.setRole(rs.getString("uloga"));
        return u;
    }
}

