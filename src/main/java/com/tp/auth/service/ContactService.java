package com.tp.auth.service;

import java.util.List;

import com.tp.auth.dao.ContactDao;
import com.tp.auth.model.Contact;

public class ContactService {

    private final ContactDao dao = new ContactDao();

    public List<Contact> list(int userId) throws Exception {
        return dao.findByUser(userId);
    }

    public List<Contact> search(int userId, String text) throws Exception {
        if (text == null || text.isBlank()) return dao.findByUser(userId);
        return dao.search(userId, text.trim());
    }

    public String add(Contact c) throws Exception {
        String error = validate(c);
        if (error != null) return error;
        dao.insert(c);
        return null;
    }

    public String update(Contact c) throws Exception {
        String error = validate(c);
        if (error != null) return error;
        dao.update(c);
        return null;
    }

    public void delete(int id, int userId) throws Exception {
        dao.delete(id, userId);
    }

    private String validate(Contact c) {
        c.setNom(clean(c.getNom()));
        c.setPrenom(clean(c.getPrenom()));
        c.setEmail(clean(c.getEmail()));
        c.setTelephone(clean(c.getTelephone()));

        if (c.getNom() == null) return "Le nom est obligatoire";
        if (c.getPrenom() == null) return "Le prénom est obligatoire";

        String email = c.getEmail();
        if (email != null && (!email.contains("@") || !email.contains("."))) {
            return "Email invalide";
        }

        String tel = c.getTelephone();
        if (tel != null && !tel.matches("\\+?[0-9 ]+")) {
            return "Téléphone invalide";
        }
        return null;
    }

    private String clean(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}