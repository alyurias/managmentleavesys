import java.util.UUID;

// Ticket.java
public class Ticket {
    private String id;
    private String category;
    private boolean approved;
    private String reason; // Novi atribut

    // Konstruktor sa reason
    public Ticket(String category, boolean approved, String reason) {
        this.id = UUID.randomUUID().toString();
        this.category = category;
        this.approved = approved;
        this.reason = reason;
    }

    // Konstruktor sa ID-om i reason
    public Ticket(String id, String category, boolean approved, String reason) {
        this.id = id;
        this.category = category;
        this.approved = approved;
        this.reason = reason;
    }

    // Getteri i setteri za id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getteri i setteri za category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getteri i setteri za approved
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    // Getteri i setteri za reason
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", approved=" + approved +
                ", reason='" + reason + '\'' +
                '}';
    }
}
