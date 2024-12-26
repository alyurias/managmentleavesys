

import java.util.ArrayList;

public class Employee {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private ArrayList<String> tickets;
    private String role;  // Dodan atribut role

    public Employee(String name, String surname, String email, String password, ArrayList<String> tickets, String role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.tickets = tickets;
        this.role = role;  // Inicijalizacija role
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getTickets() {
        return tickets;
    }

    public String getRole() {
        return role;  // Getter za role
    }
}
