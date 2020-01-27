
package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.Query;

/**
 *
 * @author Blake Thompson
 */
public class AppointmentTableRow {
    private ObservableValue<String> appointmentId;
    private ObservableValue<String> customer;
    private ObservableValue<String> type;
    private ObservableValue<String> start;
    private ObservableValue<String> end;
    private ObservableValue<String> user;
    
    private static ObservableList<AppointmentTableRow> appointmentListAll = FXCollections.observableArrayList();
    private static ObservableList<AppointmentTableRow> appointmentListWeek = FXCollections.observableArrayList();
    private static ObservableList<AppointmentTableRow> appointmentListMonth = FXCollections.observableArrayList();
    
    public AppointmentTableRow(ObservableValue<String> customerId, ObservableValue<String> type,
            ObservableValue<String> start, ObservableValue<String> end, 
            ObservableValue<String> user, ObservableValue<String> appointmentId) {
        this.customer = customerId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.user = user;
        this.appointmentId = appointmentId;
    }

    public static void clearAppointmentListMonth() {
        appointmentListMonth.clear();
    }
    public static void clearAppointmentListAll() {
        appointmentListAll.clear();
    }
    public static void clearAppointmentListWeek() { appointmentListWeek.clear(); }
    
    public static ObservableList<AppointmentTableRow> getAppointmentListWeek() {
        return appointmentListWeek;
    }
    public static void setAppointmentListWeek(ObservableList<AppointmentTableRow> appointmentListWeek) {
        AppointmentTableRow.appointmentListWeek = appointmentListWeek;
    }

    public static ObservableList<AppointmentTableRow> getAppointmentListMonth() {
        return appointmentListMonth;
    }
    public static void setAppointmentListMonth(ObservableList<AppointmentTableRow> appointmentListMonth) {
        AppointmentTableRow.appointmentListMonth = appointmentListMonth;
    }

    public static ObservableList<AppointmentTableRow> getAppointmentListAll() {
        return appointmentListAll;
    }
    public void setAppointmentListAll(ObservableList<AppointmentTableRow> appointmentListAll) {
        this.appointmentListAll = appointmentListAll;
    }

    public ObservableValue<String> getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(ObservableValue<String> appointmentId) {
        this.appointmentId = appointmentId;
    }

    public ObservableValue<String> getCustomer() {
        return customer;
    }
    public void setCustomer(ObservableValue<String> customerId) {
        this.customer = customerId;
    }

    public ObservableValue<String> getType() {
        return type;
    }
    public void setType(ObservableValue<String> type) {
        this.type = type;
    }

    public ObservableValue<String> getStart() {
        return start;
    }
    public void setStart(ObservableValue<String> start) {
        this.start = start;
    }

    public ObservableValue<String> getEnd() {
        return end;
    }
    public void setEnd(ObservableValue<String> end) {
        this.end = end;
    }

    public ObservableValue<String> getUser() {
        return user;
    }
    public void setUser(ObservableValue<String> user) {
        this.user = user;
    }
    
    public static String getAppointmentUser(String appointmentId) throws SQLException {
        String sqlStatement = "SELECT user.userName\n" +
                "FROM appointment\n" +
                "JOIN user\n" +
                "ON appointment.userId = user.userId\n" +
                "WHERE appointmentId = " + appointmentId;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        result.next();
        return result.getString("userName");
    }
    
    public static String getCustomerName(String appointmentId) throws SQLException {
        String sqlStatement = "SELECT customer.CustomerName\n" +
                "FROM appointment\n" +
                "JOIN customer\n" +
                "ON appointment.customerId = customer.customerId\n" +
                "WHERE appointmentId = " + appointmentId;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        result.next();
        return result.getString("customerName");
    }
}