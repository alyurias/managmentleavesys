// EmployeeObrazac.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeObrazac {
    private JButton logoutButton;
    private JButton addRequestButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton; // New refresh button
    private JTable requestTable;
    private JLabel nameLabel;
    private JPanel employeePanel;

    private DefaultTableModel tableModel;
    private Employee employee;
    private EmployeeService employeeService;

    public EmployeeObrazac(Employee employee) {
        this.employee = employee;
        this.employeeService = new EmployeeService();
        nameLabel = new JLabel("Welcome " + employee.getName() + " " + employee.getSurname());

        addRequestButton = new JButton("Add Request");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");

        // Create table model with columns: ID, Category, Approved
        String[] columns = {"ID", "Category", "Approved", "Reason"}; // Dodana kolona Reason
        tableModel = new DefaultTableModel(columns, 0);
        requestTable = new JTable(tableModel);

        // Set table appearance
        JScrollPane scrollPane = new JScrollPane(requestTable);
        employeePanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(nameLabel, BorderLayout.NORTH); // Add nameLabel at the top

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRequestButton); // Add buttons for CRUD operations
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton); // Add refresh button

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton); // Add logoutButton below the table

        employeePanel.add(topPanel, BorderLayout.NORTH); // Add top panel at the top
        employeePanel.add(buttonPanel, BorderLayout.NORTH); // Add button panel below the nameLabel
        employeePanel.add(scrollPane, BorderLayout.CENTER); // Add table below the button panel
        employeePanel.add(bottomPanel, BorderLayout.SOUTH); // Add logout button below the table

        // Load initial tickets for the employee
        loadEmployeeTickets();

        addRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] categories = {"Obicni", "Redovni", "Zdravstveni"};
                JComboBox<String> categoryComboBox = new JComboBox<>(categories);
                JTextField reasonField = new JTextField(20); // Polje za unos razloga

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(new JLabel("Category:"));
                panel.add(categoryComboBox);
                panel.add(new JLabel("Reason:"));
                panel.add(reasonField);

                int option = JOptionPane.showConfirmDialog(null, panel, "Add Ticket", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String category = (String) categoryComboBox.getSelectedItem();
                    String reason = reasonField.getText().trim();

                    if (!reason.isEmpty()) {
                        Ticket newTicket = new Ticket(category, false, reason); // Novi atribut reason
                        employeeService.addTicketToEmployee(employee.getId(), newTicket);

                        // Reload tickets za zaposlenog
                        loadEmployeeTickets();
                    } else {
                        JOptionPane.showMessageDialog(null, "Reason cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a ticket to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Dohvati ID odabranog ticketa iz tabele
                String ticketId = (String) tableModel.getValueAt(selectedRow, 0);
                Ticket ticketToEdit = null;

                // Pronalaženje ticketa u listi
                List<Ticket> tickets = employeeService.getTicketsForEmployee(employee.getId());
                for (Ticket ticket : tickets) {
                    if (ticket.getId().equals(ticketId)) {
                        ticketToEdit = ticket;
                        break;
                    }
                }

                if (ticketToEdit == null) {
                    JOptionPane.showMessageDialog(null, "Ticket not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kreiranje dijaloga za uređivanje
                String[] categories = {"Obicni", "Redovni", "Zdravstveni"};
                JComboBox<String> categoryComboBox = new JComboBox<>(categories);
                categoryComboBox.setSelectedItem(ticketToEdit.getCategory());

                JTextField reasonField = new JTextField(ticketToEdit.getReason(), 20);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(new JLabel("Category:"));
                panel.add(categoryComboBox);
                panel.add(new JLabel("Reason:"));
                panel.add(reasonField);

                int option = JOptionPane.showConfirmDialog(null, panel, "Edit Ticket", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String updatedCategory = (String) categoryComboBox.getSelectedItem();
                    String updatedReason = reasonField.getText().trim();

                    if (!updatedReason.isEmpty()) {
                        ticketToEdit.setCategory(updatedCategory);
                        ticketToEdit.setReason(updatedReason);

                        // Ažuriraj ticket u bazi podataka
                        employeeService.updateTicket(ticketToEdit);

                        // Ponovo učitaj listu ticketa za zaposlenog
                        loadEmployeeTickets();
                    } else {
                        JOptionPane.showMessageDialog(null, "Reason cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this ticket?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String ticketId = (String) tableModel.getValueAt(selectedRow, 0);

                        // Remove the row from the table
                        tableModel.removeRow(selectedRow);

                        // Delete the ticket from MongoDB
                        employeeService.deleteTicket(employee.getId(), ticketId);

                        // Optionally reload tickets to ensure consistency
                        loadEmployeeTickets();
                    }
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reload tickets for the employee
                loadEmployeeTickets();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setContentPane(new Login().getLoginPanel());
                loginFrame.pack();
                loginFrame.setVisible(true);
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(employeePanel);
                topFrame.dispose(); // Close current EmployeeObrazac window
                loginFrame.setVisible(true); // Show login frame again
            }
        });
    }

    public JPanel getEmployeePanel() {
        return employeePanel;
    }

    private void loadEmployeeTickets() {
        List<Ticket> tickets = employeeService.getTicketsForEmployee(employee.getId());
        tableModel.setRowCount(0); // Očistiti postojeće redove
        for (Ticket ticket : tickets) {
            String status = ticket.isApproved() ? "Prihvaćeno" : "Čeka na odobrenje";
            tableModel.addRow(new Object[]{ticket.getId(), ticket.getCategory(), status, ticket.getReason()});
        }
    }
}
