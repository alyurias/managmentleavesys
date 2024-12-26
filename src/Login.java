import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JTextField email;
    private JPasswordField password;
    private JButton submitButton;
    private JPanel loginPanel;
    private EmployeeService employeeService;

    public Login() {
        employeeService = new EmployeeService();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailText = email.getText();
                String passwordText = new String(password.getPassword());

                Employee employee = employeeService.authenticate(emailText, passwordText);
                if (employee != null) {
                    String role = employee.getRole();
                    switch (role) {
                        case "Employee":
                            // Prebacivanje na Employee panel
                            showEmployeePanel();
                            break;
                        case "Manager":
                            // Prebacivanje na Manager panel
                            showManagerPanel();
                            break;
                        case "Admin":
                            // Prebacivanje na Admin panel
                            showAdminPanel();
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid role");
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password");
                }
            }
        });
    }

    // Metode za prikaz panela (moguće su prilagodbe u skladu sa implementacijom panela)
    private void showEmployeePanel() {
        JFrame frame = new JFrame("Employee Dashboard");
        frame.setContentPane(new EmployeeObrazac().getEmployeePanel());
        frame.pack();
        frame.setVisible(true);
    }

    private void showManagerPanel() {
        JFrame frame = new JFrame("Manager Dashboard");
        // frame.setContentPane(new ManagerObrazac().getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    private void showAdminPanel() {
        JFrame frame = new JFrame("Admin Dashboard");
        // frame.setContentPane(new AdminObrazac().getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    // Main funkcija za pokretanje aplikacije
    public static void main(String[] args) {
        // Kreiranje i postavljanje izgleda za Login
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new Login().getLoginPanel());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
