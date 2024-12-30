import java.util.UUID;
import java.util.Date;

public class Ticket {
    private String id;
    private String category;
    private String approved; // Promijenjeno iz boolean u String
    private String reason;
    private Date startTicketDate;
    private Date endTicketDate;

    // Konstruktor za kreiranje novog zahtjeva (podrazumijevano stanje "na cekanju")
    public Ticket(String category, boolean b, String reason, Date startTicketDate, Date endTicketDate) {
        this.id = UUID.randomUUID().toString();
        this.category = category;
        this.approved = "na cekanju"; // Podrazumijevana vrijednost
        this.reason = reason;
        this.startTicketDate = startTicketDate;
        this.endTicketDate = endTicketDate;
    }

    // Konstruktor za kreiranje objekta s eksplicitno navedenim podacima
    public Ticket(String id, String category, String approved, String reason, Date startTicketDate, Date endTicketDate) {
        this.id = id;
        this.category = category;
        this.approved = approved;
        this.reason = reason;
        this.startTicketDate = startTicketDate;
        this.endTicketDate = endTicketDate;
    }

    // Getteri i setteri
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartTicketDate() {
        return startTicketDate;
    }

    public void setStartTicketDate(Date startTicketDate) {
        this.startTicketDate = startTicketDate;
    }

    public Date getEndTicketDate() {
        return endTicketDate;
    }

    public void setEndTicketDate(Date endTicketDate) {
        this.endTicketDate = endTicketDate;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", approved='" + approved + '\'' +
                ", reason='" + reason + '\'' +
                ", startTicketDate=" + startTicketDate +
                ", endTicketDate=" + endTicketDate +
                '}';
    }

    public Object isApproved() {

        return null;
    }
}
