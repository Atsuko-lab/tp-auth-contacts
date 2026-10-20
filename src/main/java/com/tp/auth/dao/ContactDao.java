package com.tp.auth.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.tp.auth.db.Database;
import com.tp.auth.model.Contact;

public class ContactDao {

    public List<Contact> findByUser(int userId) throws Exception {
        String sql = "SELECT id, user_id, nom, prenom, email, telephone FROM contacts "
                   + "WHERE user_id = ? ORDER BY nom, prenom";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return readList(ps);
        }
    }

    public void insert(Contact c) throws Exception {
        String sql = "INSERT INTO contacts (user_id, nom, prenom, email, telephone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getPrenom());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getTelephone());
            ps.executeUpdate();
        }
    }

    public void update(Contact c) throws Exception {
        String sql = "UPDATE contacts SET nom = ?, prenom = ?, email = ?, telephone = ? "
                   + "WHERE id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelephone());
            ps.setInt(5, c.getId());
            ps.setInt(6, c.getUserId());
            ps.executeUpdate();
        }
    }

    public void delete(int id, int userId) throws Exception {
        String sql = "DELETE FROM contacts WHERE id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public List<Contact> search(int userId, String text) throws Exception {
        String sql = "SELECT id, user_id, nom, prenom, email, telephone FROM contacts "
                   + "WHERE user_id = ? AND (nom LIKE ? OR prenom LIKE ? OR email LIKE ? OR telephone LIKE ?) "
                   + "ORDER BY nom, prenom";
        String pattern = "%" + text + "%";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setString(5, pattern);
            return readList(ps);
        }
    }

    private List<Contact> readList(PreparedStatement ps) throws Exception {
        List<Contact> contacts = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone")));
            }
        }
        return contacts;
    }
}