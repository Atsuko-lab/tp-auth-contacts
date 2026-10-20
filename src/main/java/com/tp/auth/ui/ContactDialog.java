package com.tp.auth.ui;

import java.awt.*;
import javax.swing.*;

import com.tp.auth.model.Contact;
import com.tp.auth.service.ContactService;

public class ContactDialog extends JDialog {

    private JTextField nomField = new JTextField(20);
    private JTextField prenomField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JTextField telephoneField = new JTextField(20);
    private JLabel errorLabel = new JLabel(" ");
    private boolean saved = false;

    public ContactDialog(JFrame owner, ContactService service, int userId, Contact contact) {
        super(owner, contact == null ? "Ajouter un contact" : "Modifier un contact", true);

        if (contact != null) {
            nomField.setText(contact.getNom());
            prenomField.setText(contact.getPrenom());
            emailField.setText(contact.getEmail());
            telephoneField.setText(contact.getTelephone());
        }

        JButton saveBtn = new JButton("Enregistrer");
        JButton cancelBtn = new JButton("Annuler");

        saveBtn.addActionListener(e -> {
            int id = (contact == null) ? 0 : contact.getId();
            Contact c = new Contact(id, userId, nomField.getText(), prenomField.getText(),
                    emailField.getText(), telephoneField.getText());
            try {
                String error = (contact == null) ? service.add(c) : service.update(c);
                if (error == null) {
                    saved = true;
                    dispose();
                } else {
                    errorLabel.setText(error);
                }
            } catch (Exception ex) {
                errorLabel.setText("Erreur : " + ex.getMessage());
            }
        });
        cancelBtn.addActionListener(e -> dispose());

        setLayout(new GridLayout(6, 2, 8, 8));
        add(new JLabel("Nom :"));
        add(nomField);
        add(new JLabel("Prénom :"));
        add(prenomField);
        add(new JLabel("Email :"));
        add(emailField);
        add(new JLabel("Téléphone :"));
        add(telephoneField);
        add(errorLabel);
        add(new JLabel());
        add(saveBtn);
        add(cancelBtn);

        errorLabel.setForeground(Color.RED);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSaved() {
        return saved;
    }
}