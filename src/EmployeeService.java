// EmployeeService.java
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private final MongoCollection<Document> collection;

    public EmployeeService() {
        MongoDatabase database = MongoDBController.getInstance().getDatabase();
        this.collection = database.getCollection("kolekcija");
    }

    // Method for authenticating users
    public Employee authenticate(String email, String password) {
        Document doc = collection.find(Filters.eq("email", email)).first();

        if (doc != null) {
            String storedPassword = doc.getString("password");

            if (storedPassword.equals(password)) {
                String id = doc.getString("_id"); // Change to getString
                String name = doc.getString("name");
                String surname = doc.getString("surname");
                String role = doc.getString("role");
                List<String> tickets = (List<String>) doc.get("tickets");

                return new Employee(id, name, surname, email, storedPassword, new ArrayList<>(tickets), role);
            } else {
                System.out.println("Passwords do not match.");
            }
        } else {
            System.out.println("User with this email doesn't exist.");
        }
        return null;
    }

    // Method for adding a ticket to an employee
    public void addTicketToEmployee(String employeeId, Ticket ticket) {
        // Create a new document for the ticket
        Document ticketDoc = new Document("_id", ticket.getId()) // Use string ID
                .append("type", "ticket")
                .append("id", ticket.getId())
                .append("category", ticket.getCategory())
                .append("approved", ticket.isApproved());

        collection.insertOne(ticketDoc);

        // Update the employee's tickets array
        Document query = new Document("_id", employeeId); // Use string ID
        collection.updateOne(query, Updates.push("tickets", ticket.getId()));
    }

    // Method to get all tickets for an employee
    public List<Ticket> getTicketsForEmployee(String employeeId) {
        Document employeeDoc = collection.find(Filters.eq("_id", employeeId)).first(); // Use string ID
        List<String> ticketIds = (List<String>) employeeDoc.get("tickets");

        List<Ticket> tickets = new ArrayList<>();
        for (String ticketId : ticketIds) {
            Document ticketDoc = collection.find(Filters.eq("id", ticketId)).first();
            if (ticketDoc != null) {
                String id = ticketDoc.getString("id");
                String category = ticketDoc.getString("category");
                boolean approved = ticketDoc.getBoolean("approved");
                tickets.add(new Ticket(id, category, approved));  // Use the appropriate constructor
            }
        }
        return tickets;
    }
}
