
package controllers;

import data.Appointment;
import data.AppointmentTableRow;
import implement.DatabaseReadWrite;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import utilities.DateTimeLocale;
import utilities.Query;

/**
 *
 * @author Blake Thompson
 */
public class AppointmentsMenuController implements Initializable {
    Stage stage;
    Parent scene;

    // holds the tablerow selected from the Main Menu screen
    private static AppointmentTableRow apptModify;
    
    @FXML
    private Label variableAppointmentTitle;

    @FXML
    private ComboBox<String> customerNameComboBox;

    @FXML
    private ComboBox<String> appointmentTypeComboBox;

    @FXML
    private ComboBox<String> userNameComboBox;

    @FXML
    private DatePicker dateCombobox;

    @FXML
    private ComboBox<String> hourCombobox;

    @FXML
    private ComboBox<String> minuteCombobox;

    @FXML
    private ComboBox<String> amPmCombobox;

    @FXML
    private TextField AppointmentLengthText;

    @FXML
    private ComboBox<String> AppointmentLengthUnitDropdown;

    @FXML
    void OnActionDisplayMainMenu(ActionEvent event) throws IOException {
        MainMenuController.newAppointmentFlag = false;
        MainMenuController.modifyAppointmentFlag = false;
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void OnActionSaveAppointment(ActionEvent event) {
        try {
            // get appointmentId if modify or set 0 for new
            String apptID = null;
            if (MainMenuController.newAppointmentFlag)
                apptID = "0";
            else if (MainMenuController.modifyAppointmentFlag)
                apptID = apptModify.getAppointmentId().getValue();
            
            //get customer ID from name
            String sqlStatement = "SELECT customerId FROM customer WHERE customerName = '" +
                    customerNameComboBox.getValue() + "'";
            Query.makeQuery(sqlStatement);
            ResultSet customerNameRS = Query.getResult();
            customerNameRS.next();
            String customerId = customerNameRS.getString("customerId");
            
            //get user ID from name
            sqlStatement = "SELECT userId FROM user WHERE userName = '" +
                    userNameComboBox.getValue() + "'";
            Query.makeQuery(sqlStatement);
            ResultSet userNameRS = Query.getResult();
            userNameRS.next();
            String userId = userNameRS.getString("userId");
            
            //convert start time to 24h UTC
            String start = DateTimeLocale.localToUtc(dateCombobox.getValue() + " " +
                    hourCombobox.getValue() + minuteCombobox.getValue() + " " +
                    amPmCombobox.getValue());
            
            //convert appointment length to end time
            int minutes;
            
            if (AppointmentLengthUnitDropdown.getValue().equals("hours"))
                minutes = Integer.parseInt(AppointmentLengthText.getText()) * 60;
            else
                minutes = Integer.parseInt(AppointmentLengthText.getText());
            
            String end = DateTimeLocale.calculateAppointmentEnd(start, minutes);

            // check that time falls between business hours
            if (!(DateTimeLocale.checkBusinessHours(hourCombobox.getValue() + minuteCombobox.getValue() + " " +
                    amPmCombobox.getValue(), DateTimeLocale.utcToLocal(end.replace("T", " ") + ":00")))) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please ensure appointment"
                        + " falls between 8 AM\nand 5 PM.");
                alert.showAndWait();
            }
            
            // check for appointment overlaps
            if (DateTimeLocale.isOverlapping(apptID, userId, start, end)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please ensure appointment"
                + " does not overlap.");
                alert.showAndWait();
            }
            // if checks pass proceed with object creation
            else {
                Appointment appointment = new Appointment(apptID, customerId, userId, "not needed",
                        "not needed", "not needed", "not needed", appointmentTypeComboBox.getValue(),
                        "not needed", start, end, DateTimeLocale.getCurrentUTC(),
                        LoginMenuController.currentUser, "", LoginMenuController.currentUser);
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save this appointment?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && (result.get() == ButtonType.OK)) {
                    if (MainMenuController.newAppointmentFlag)
                        DatabaseReadWrite.insertAppointment(appointment);
                    else if (MainMenuController.modifyAppointmentFlag)
                        DatabaseReadWrite.updateAppointment(appointment);
                    
                    MainMenuController.newAppointmentFlag = false;
                    MainMenuController.modifyAppointmentFlag = false;
                    
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        } catch (SQLException | ParseException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    void initializeAppointmentType() {
        String sqlStatement = "SELECT type FROM appointment";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        appointmentTypeComboBox.setEditable(true);
        try {
            result.beforeFirst();
            while (result.next()) {
                appointmentTypeComboBox.getItems().add(result.getString("type"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void initializeCustomerName() {
        String sqlStatement = "SELECT customerName FROM customer";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        try {
            result.beforeFirst();
            while (result.next()) {
                customerNameComboBox.getItems().add(result.getString("customerName"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void initializeUser() {
        String sqlStatement = "SELECT userName FROM user";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        
        try {
            result.beforeFirst();
            while (result.next()) {
                userNameComboBox.getItems().add(result.getString("userName"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // limits date selections to only M-F, no past dates
    void initializeDate() {
        // lambda expression uses the Callback interface to use our defined method
        // when the date picker object is called. It passes the method as a parameter
        // to the DateCell object in order to define the DateCell
        Callback<DatePicker, DateCell> callB = (final DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || item.compareTo(today) < 0);
                if (item.getDayOfWeek() == DayOfWeek.SATURDAY
                        || item.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setDisable(true);
            }
        };
        dateCombobox.setEditable(false);
        dateCombobox.setDayCellFactory(callB);
    }
    
    void initializeHour() {
        String hours[] = {"8", "9", "10", "11", "12", "1", "2", "3", "4", "5"};
        for (int i = 0; i <= 9; i++) {
            hourCombobox.getItems().add(hours[i]);
        }
    }
    
    void initializeMinute() {
        String minutes[] = {":00", ":15", ":30", ":45"};
        
        for (int i = 0; i <4; i++) {
            minuteCombobox.getItems().add(minutes[i]);
        }
    }
    
    void initializeAmPm() {
        amPmCombobox.getItems().add("AM");
        amPmCombobox.getItems().add("PM");
    }
    
    void initializeTimeUnit() {
        AppointmentLengthUnitDropdown.getItems().add("minutes");
        AppointmentLengthUnitDropdown.getItems().add("hours");
    }
    
    public static void initializeModifyAppointment(AppointmentTableRow appointment) {
        apptModify = appointment;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // call all combobox initializations
        initializeAppointmentType();
        initializeCustomerName();
        initializeUser();
        initializeHour();
        initializeMinute();
        initializeAmPm();
        initializeTimeUnit();
        initializeDate();
        
        // new appointment stays blank
        if (MainMenuController.newAppointmentFlag) {
            variableAppointmentTitle.setText("New Appointment");
        }
        
        // modify appointment populates fields with selected appointment
        else if (MainMenuController.modifyAppointmentFlag) {
            variableAppointmentTitle.setText("Modify Appointment");
            
            // populate data from selected appointment
            try {
                customerNameComboBox.getSelectionModel().select(apptModify.getCustomer().getValue());
                appointmentTypeComboBox.getSelectionModel().select(apptModify.getType().getValue());
                userNameComboBox.getSelectionModel().select(apptModify.getUser().getValue());
                dateCombobox.setValue(DateTimeLocale.getDateFromDateTime(apptModify.getStart().getValue()));
                // parsing the time requires splitting the string multiple times
                // first split the date from the time, then time and AM/PM, then hour/minute
                // this may be better accomplished with substrings, but it works
                String splitDateTime[] = apptModify.getStart().getValue().split(" - ");
                String splitAmPm[] = splitDateTime[1].split(" ");
                String splitHourMinute[] = splitAmPm[0].split(":");
                if (splitHourMinute[0].startsWith("0"))
                    hourCombobox.getSelectionModel().select(splitHourMinute[0].substring(1));
                else
                    hourCombobox.getSelectionModel().select(splitHourMinute[0]);
                minuteCombobox.getSelectionModel().select(":" + splitHourMinute[1]);
                amPmCombobox.getSelectionModel().select(splitAmPm[1]);
                // split length from minutes/hours
                String splitMinutes[] = apptModify.getEnd().getValue().split(" ");
                AppointmentLengthText.setText(splitMinutes[0]);
                AppointmentLengthUnitDropdown.getSelectionModel().select(splitMinutes[1]);
            } catch (ParseException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}