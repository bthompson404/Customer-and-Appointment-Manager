
package data;

/**
 *
 * @author Blake Thompson
 */
public class Appointment extends DbEntry{
    private String appointmentId;
    private String customerId;
    private String userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String start;
    private String end;
    
    public static String tableFields = "customerId, userId, title, description, "
            + "location, contact, type, url, start, end, createDate, createdby,"
            + "lastUpdateBy";

    public Appointment(String appointmentId, String customerId, String userId, String title,
            String description, String location, String contact, String type,
            String url, String start, String end, String createDate, String createdBy,
            String lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static String getTableFields() {
        return tableFields;
    }
    public static void setTableFields(String tableFields) {
        Appointment.tableFields = tableFields;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
}