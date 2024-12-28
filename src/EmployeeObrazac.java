// EmployeeObrazac.java
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    private JButton refreshButton; // New refresh button
    private JTable requestTable;
    private JLabel nameLabel;
    private JPanel employeePanel;

    private DefaultTableModel tableModel;
    private Employee employee;

    public EmployeeObrazac(Employee employee) {
        this.employee = employee;
        nameLabel.setText("Welcome " + employee.getName() + " " + employee.getSurname());

        // Create table model with columns: ID, Category, Approved
        String[] columns = {"ID", "Category", "Approved"};
        tableModel = new DefaultTableModel(columns, 0);
        requestTable = new JTable(tableModel);

        // Set custom renderer for status column
        requestTable.getColumnModel().getColumn(2).setCellRenderer(new StatusColumnCellRenderer());

        // Set table appearance
        JScrollPane scrollPane = new JScrollPane(requestTable);
        employeePanel.setLayout(new BorderLayout());

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

        addRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField ticketIdField = new JTextField(20);
                ticketIdField.setText(UUID.randomUUID().toString());
                ticketIdField.setEditable(false); // ID cannot be changed

                String[] categories = {"Obicni", "Redovni", "Zdravstveni"};
                JComboBox<String> categoryComboBox = new JComboBox<>(categories);

                JPanel panel = new JPanel();
                panel.add(new JLabel("Ticket ID:"));
                panel.add(ticketIdField);
                panel.add(new JLabel("Category:"));
                panel.add(categoryComboBox);

                int option = JOptionPane.showConfirmDialog(null, panel, "Add Ticket", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String ticketId = ticketIdField.getText();
                    String category = (String) categoryComboBox.getSelectedItem();
                    String status = "Čeka na odobrenje";

                    tableModel.addRow(new Object[]{ticketId, category, status});
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow != -1) {
                    String ticketId = (String) tableModel.getValueAt(selectedRow, 0);
                    String category = (String) tableModel.getValueAt(selectedRow, 1);

                    JTextField ticketIdField = new JTextField(ticketId);
                    ticketIdField.setEditable(false); // ID cannot be changed

                    String[] categories = {"Obicni", "Redovni", "Zdravstveni"};
                    JComboBox<String> categoryComboBox = new JComboBox<>(categories);
                    categoryComboBox.setSelectedItem(category);

                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Ticket ID:"));
                    panel.add(ticketIdField);
                    panel.add(new JLabel("Category:"));
                    panel.add(categoryComboBox);

                    int option = JOptionPane.showConfirmDialog(null, panel, "Edit Ticket", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        tableModel.setValueAt(categoryComboBox.getSelectedItem(), selectedRow, 1);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this ticket?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to refresh table data
                tableModel.fireTableDataChanged();
                requestTable.clearSelection(); // Unselect any selected row
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
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

    private class StatusColumnCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;
            if ("Prihvaćeno".equals(status)) {
                cell.setBackground(Color.GREEN);
            } else if ("Čeka na odobrenje".equals(status)) {
                cell.setBackground(Color.YELLOW);
            } else if ("Odbijeno".equals(status)) {
                cell.setBackground(Color.RED);
            } else {
                cell.setBackground(Color.WHITE);
            }
            return cell;
        }
    }
}
