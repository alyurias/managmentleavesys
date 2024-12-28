// Ticket.java
import java.util.UUID;

public class Ticket {
    private String id;
    private String category;
    private boolean approved;

    public Ticket(String category, boolean approved) {
        this.id = UUID.randomUUID().toString(); // Generisanje jedinstvenog ID-a
        this.category = category;
        this.approved = approved;
    }

    public Ticket(String id, String category, boolean approved) { // Dodavanje novog konstruktora
        this.id = id;
        this.category = category;
        this.approved = approved;
    }

    // Getteri i setteri
    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", approved=" + approved +
                '}';
    }
}
