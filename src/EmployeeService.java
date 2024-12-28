
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private final MongoCollection<Document> collection;

    public EmployeeService() {
        MongoDatabase database = MongoDBController.getInstance().getDatabase();
        this.collection = database.getCollection("kolekcija");
    }

    // Metoda za autentifikaciju korisnika
    public Employee authenticate(String email, String password) {

        // Pronalaženje korisnika prema emailu
        Document doc = collection.find(Filters.eq("email", email)).first();

        if (doc != null) {
            String storedPassword = doc.getString("password");

            // Provjera da li se unesena lozinka poklapa sa lozinkom u bazi
            if (storedPassword.equals(password)) {
                String id = doc.getString("_id");
                String name = doc.getString("name");
                String surname = doc.getString("surname");
                String role = doc.getString("role");
                List<String> tickets = (List<String>) doc.get("tickets");

                // Kreiranje i vraćanje Employee objekta
                return new Employee(name, surname, email, storedPassword, new ArrayList<>(tickets), role);
            } else {
                System.out.println("Lozinke se ne poklapaju.");
            }
        } else {
            System.out.println("Korisnik sa tim emailom ne postoji.");
        }
        return null; // Ako autentifikacija ne uspije
    }
}
