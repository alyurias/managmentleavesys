import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class EmployeeObrazac {
    private JButton logoutButton;
    private JButton addRequestButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable requestTable;
    private JLabel nameLabel;
    private JPanel employeePanel;

    private DefaultTableModel tableModel;

    public EmployeeObrazac() {
        // Kreiranje modela tabele sa kolonama: ID, Category, Approved
        String[] columns = {"ID", "Category", "Approved"};
        tableModel = new DefaultTableModel(columns, 0);
        requestTable = new JTable(tableModel);

        // Podesi izgled tabele
        JScrollPane scrollPane = new JScrollPane(requestTable);
        employeePanel.setLayout(new BorderLayout());
        employeePanel.add(scrollPane, BorderLayout.CENTER);

        // Pop-up za dodavanje novog tiketa
        addRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kreiranje pop-up prozora za unos tiketa
                JTextField ticketIdField = new JTextField(20);
                ticketIdField.setText(UUID.randomUUID().toString());
                ticketIdField.setEditable(false); // ID ne može biti izmijenjen

                String[] categories = {"Obicni", "Redovni", "Zdravstveni"};
                JComboBox<String> categoryComboBox = new JComboBox<>(categories);

                JPanel panel = new JPanel();
                panel.add(new JLabel("Ticket ID:"));
                panel.add(ticketIdField);
                panel.add(new JLabel("Category:"));
                panel.add(categoryComboBox);

                int option = JOptionPane.showConfirmDialog(null, panel, "Add Ticket", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    // Dodavanje tiketa u tabelu
                    String ticketId = ticketIdField.getText();
                    String category = (String) categoryComboBox.getSelectedItem();
                    String status = "Čeka na odobrenje";

                    // Dodajemo novi red u tabelu
                    tableModel.addRow(new Object[]{ticketId, category, status});
                }
            }
        });

        // Editovanje postojećeg tiketa
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow != -1) {
                    String ticketId = (String) tableModel.getValueAt(selectedRow, 0);
                    String category = (String) tableModel.getValueAt(selectedRow, 1);
                    String status = (String) tableModel.getValueAt(selectedRow, 2);

                    JTextField ticketIdField = new JTextField(ticketId);
                    ticketIdField.setEditable(false); // ID ne može biti izmijenjen

                    String[] categories = {"Obicni", "Redovni", "Zdravstveni"};
                    JComboBox<String> categoryComboBox = new JComboBox<>(categories);
                    categoryComboBox.setSelectedItem(category);

                    String[] statuses = {"Čeka na odobrenje", "Odbijeno", "Prihvaćeno"};
                    JComboBox<String> statusComboBox = new JComboBox<>(statuses);
                    statusComboBox.setSelectedItem(status);

                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Ticket ID:"));
                    panel.add(ticketIdField);
                    panel.add(new JLabel("Category:"));
                    panel.add(categoryComboBox);
                    panel.add(new JLabel("Status:"));
                    panel.add(statusComboBox);

                    int option = JOptionPane.showConfirmDialog(null, panel, "Edit Ticket", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        // Ažuriramo podatke u tabeli
                        tableModel.setValueAt(categoryComboBox.getSelectedItem(), selectedRow, 1);
                        tableModel.setValueAt(statusComboBox.getSelectedItem(), selectedRow, 2);
                    }
                }
            }
        });

        // Brisanje tiketa
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this ticket?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Brišemo tiket iz tabele
                        tableModel.removeRow(selectedRow);
                    }
                }
            }
        });

        // Logout dugme - vraća na login ekran
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setContentPane(new Login().getLoginPanel());
                loginFrame.pack();
                loginFrame.setVisible(true);
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(employeePanel);
                topFrame.dispose(); // Zatvara trenutni EmployeeObrazac prozor
            }
        });
    }

    public JPanel getEmployeePanel() {
        return employeePanel;
    }

}
