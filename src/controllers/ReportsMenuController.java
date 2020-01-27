
package controllers;

import implement.DatabaseReadWrite;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.time.LocalDate;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.DateTimeLocale;
import utilities.Query;

/**
 *
 * @author Blake Thompson
 */
public class ReportsMenuController implements Initializable {
    Stage stage;
    Parent scene;
    
    private static final DateFormatSymbols dfs = new DateFormatSymbols();
    private final String[] months = dfs.getMonths();
    
    @FXML
    private ComboBox<String> reportCombobox;

    @FXML
    private TextArea reportTextArea;

    @FXML
    void OnActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionGenerateReport(ActionEvent event) throws SQLException, IOException {
        reportTextArea.clear();

        String sqlStatement = "SELECT type, start FROM appointment";
        Query.makeQuery(sqlStatement);
        ResultSet rs = Query.getResult();
        
        switch (reportCombobox.getSelectionModel().getSelectedItem()) {
            default:
                break;
            case ("Appointment Types by Month"):
                appointmentTypesByMonth(rs);
                break;
            case ("Consultant Schedules"):
                getConsultantSchedule(rs);
                break;
            case("Login Report"):
                loginReport();
                break;
        }
    }
    
    private void loginReport() throws IOException {
        Path path = Paths.get(LoginMenuController.logfile);
        
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String st;
            // loop through read and print each line
            while ((st = br.readLine()) != null)
            reportTextArea.appendText(st + "\n");
        }
    }
    
    private void getConsultantSchedule(ResultSet rs) {
        ArrayList<String> userNames = new ArrayList<>(0);
        ArrayList<ResultSet> userSchedules = new ArrayList<>(0);
                
        // get all usernames
        String sqlStatement = "SELECT userName, userId from user";
        Query.makeQuery(sqlStatement);
        rs = Query.getResult();
                
        // loop through result set, put usernames into getUserSchedule(), add username
        // and schedule to same index in separate arrays
        try {
            rs.beforeFirst();
            while (rs.next()) {
                String userName = rs.getString("userName");
                String userId = rs.getString("userId");
                ResultSet schedule = DatabaseReadWrite.getUserSchedule(userId);

                userNames.add(userName);
                userSchedules.add(schedule);
            }
                    
            // print all schedules
            for (int i = 0; i < userNames.size(); i++) {
                reportTextArea.setText("\nAppointments for user '" + userNames.get(i) + "':\n\n");
                userSchedules.get(i).beforeFirst();

                while (userSchedules.get(i).next()) {
                    String start = DateTimeLocale.utcToLocal(userSchedules.get(i).getString("start"));
                    String end = DateTimeLocale.utcToLocal(userSchedules.get(i).getString("end"));
                    reportTextArea.appendText(start + " to " + end + "\n");
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void appointmentTypesByMonth(ResultSet rs) {
        final String OLD_FORMAT = "yyyy-dd-MM";
        final String NEW_FORMAT = "dd/MM/yyyy";
        
        int[] monthArray = new int[12];
        ArrayList<String> typeArray = new ArrayList<>(0);
                
        try {
            // outer loop iterates 12 times, one for each month
            for (int i = 0; i < 12; i++){
                // inner loop iterates through resultset object
                rs.beforeFirst();
                while (rs.next()){
                    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                    Date d = sdf.parse(rs.getString("start").substring(0, 10));
                            
                    sdf.applyPattern(NEW_FORMAT);
                    String start = sdf.format(d);
                    LocalDate date = DateTimeLocale.getDateFromDateTime(start);
                    int dateValueIndex = date.getMonthValue() - 1;
                    String type = rs.getString("type");
                            
                    // branch if the resultset line is the same month as the current iteration
                    if (dateValueIndex == i) {
                        // if the type has not been found for this month yet, add it to the array and increment month
                        // array position by one
                            if (!(typeArray.contains(type))) {
                                typeArray.add(type);
                                monthArray[dateValueIndex]++;
                            }
                    }
                }
            }
                    
            reportTextArea.setText("Number of Appointment Types by Month: \n\n");
                    
            // iterate through and print each month name plus the corresponding monthArray position
            for (int w = 0; w < 12; w++) 
                reportTextArea.appendText(months[w] + ": " + monthArray[w] + "\n");
                    
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportCombobox.getItems().add("Appointment Types by Month");
        reportCombobox.getItems().add("Consultant Schedules");
        reportCombobox.getItems().add("Login Report");
        reportTextArea.setEditable(false);
    }
}
