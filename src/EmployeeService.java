import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeService {
    private final MongoCollection<Document> collection;

    public EmployeeService() {
        MongoDatabase database = MongoDBController.getInstance().getDatabase();
        this.collection = database.getCollection("kolekcija");
    }

    // Metoda za autentifikaciju korisnika
    public Employee authenticate(String email, String password) {
        Document doc = collection.find(Filters.eq("email", email)).first();

        if (doc != null) {
            String storedPassword = doc.getString("password");

            if (storedPassword.equals(password)) {
                String id = doc.getString("_id");
                String name = doc.getString("name");
                String surname = doc.getString("surname");
                String role = doc.getString("role");
                List<String> tickets = (List<String>) doc.get("tickets");

                return new Employee(id, name, surname, email, storedPassword, new ArrayList<>(tickets), role);
            } else {
                System.out.println("Lozinke se ne podudaraju.");
            }
        } else {
            System.out.println("Korisnik sa datim emailom ne postoji.");
        }
        return null;
    }

    // Dodavanje zahtjeva zaposleniku
    public void addTicketToEmployee(String employeeId, Ticket ticket) {
        Document ticketDoc = new Document("_id", ticket.getId())
                .append("category", ticket.getCategory())
                .append("approved", ticket.getApproved())
                .append("reason", ticket.getReason())
                .append("startTicketDate", ticket.getStartTicketDate())
                .append("endTicketDate", ticket.getEndTicketDate());

        collection.insertOne(ticketDoc);

        Document query = new Document("_id", employeeId);
        collection.updateOne(query, Updates.push("tickets", ticket.getId()));
    }

    // Ažuriranje zahtjeva
    public void updateTicket(Ticket ticket) {
        Document query = new Document("_id", ticket.getId());
        Document update = new Document("$set", new Document("category", ticket.getCategory())
                .append("approved", ticket.getApproved())
                .append("reason", ticket.getReason())
                .append("startTicketDate", ticket.getStartTicketDate())
                .append("endTicketDate", ticket.getEndTicketDate()));

        collection.updateOne(query, update);
    }

    // Brisanje zahtjeva
    public void deleteTicket(String employeeId, String ticketId) {
        Document query = new Document("_id", ticketId);
        collection.deleteOne(query);

        Document employeeQuery = new Document("_id", employeeId);
        collection.updateOne(employeeQuery, Updates.pull("tickets", ticketId));
    }

    // Dohvatanje svih zahtjeva za zaposlenika
    public List<Ticket> getTicketsForEmployee(String employeeId) {
        Document employeeDoc = collection.find(Filters.eq("_id", employeeId)).first();
        List<String> ticketIds = (List<String>) employeeDoc.get("tickets");

        List<Ticket> tickets = new ArrayList<>();
        for (String ticketId : ticketIds) {
            Document ticketDoc = collection.find(Filters.eq("_id", ticketId)).first();
            if (ticketDoc != null) {
                String id = ticketDoc.getString("_id");
                String category = ticketDoc.getString("category");
                String approved = ticketDoc.getString("approved");
                String reason = ticketDoc.getString("reason");
                Date startTicketDate = ticketDoc.getDate("startTicketDate");
                Date endTicketDate = ticketDoc.getDate("endTicketDate");

                tickets.add(new Ticket(id, category, approved, reason, startTicketDate, endTicketDate));
            }
        }
        return tickets;
    }
}
