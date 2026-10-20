package com.tp.auth.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.tp.auth.model.Contact;
import com.tp.auth.model.User;
import com.tp.auth.service.ContactService;

public class ContactFrame extends JFrame {

    private User user;
    private ContactService service = new ContactService();
    private List<Contact> contacts = new ArrayList<>();
    private JTextField searchField = new JTextField(20);

    private DefaultTableModel model = new DefaultTableModel(
            new Object[] { "Nom", "Prénom", "Email", "Téléphone" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable table = new JTable(model);

    public ContactFrame(User user) {
        super("Mes contacts");
        this.user = user;

        JButton logoutBtn = new JButton("Se déconnecter");
        logoutBtn.addActionListener(e -> logout());

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("Connecté en tant que " + user.getUsername()), BorderLayout.WEST);
        userPanel.add(logoutBtn, BorderLayout.EAST);

        JButton searchBtn = new JButton("Rechercher");
        searchBtn.addActionListener(e -> refreshTable());
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher :"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.add(userPanel, BorderLayout.NORTH);
        top.add(searchPanel, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);

        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        addBtn.addActionListener(e -> openDialog(null));
        editBtn.addActionListener(e -> editContact());
        deleteBtn.addActionListener(e -> deleteContact());

        JPanel bottom = new JPanel();
        bottom.add(addBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.add(top, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);
        setContentPane(content);

        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        refreshTable();
    }

    private void refreshTable() {
        try {
            contacts = service.search(user.getId(), searchField.getText());
            model.setRowCount(0);
            for (Contact c : contacts) {
                model.addRow(new Object[] {
                        c.getNom(), c.getPrenom(), c.getEmail(), c.getTelephone()
                });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void editContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact");
            return;
        }
        openDialog(contacts.get(row));
    }

    private void openDialog(Contact contact) {
        ContactDialog dialog = new ContactDialog(this, service, user.getId(), contact);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshTable();
        }
    }

    private void deleteContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact");
            return;
        }
        Contact c = contacts.get(row);
        int choice = JOptionPane.showConfirmDialog(this,
                "Supprimer " + c.getNom() + " " + c.getPrenom() + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                service.delete(c.getId(), user.getId());
                refreshTable();
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}