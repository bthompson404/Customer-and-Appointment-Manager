
package controllers;

import data.*;
import implement.DatabaseReadWrite;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.*;

/**
 *
 * @author Blake Thompson
 */
public class MainMenuController implements Initializable {
    
    Alert alert;
    Stage stage;
    Parent scene;
    
    // context switching flags
    public static boolean loginFlag = false;
    public static boolean newCustomerFlag = false;
    public static boolean modifyCustomerFlag = false;
    public static boolean newAppointmentFlag = false;
    public static boolean modifyAppointmentFlag = false;
    
      @FXML
    private RadioButton sortByAllRb;

    @FXML
    private ToggleGroup sortByToggleGroup;

    @FXML
    private RadioButton sortByWeekRb;

    @FXML
    private RadioButton SortByMonthRb;
    
    @FXML
    private TableView<AppointmentTableRow> appointmentTable;

    @FXML
    private TableColumn<AppointmentTableRow, String> appointmentIdColumn;
    
    @FXML
    private TableColumn<AppointmentTableRow, String> appointmentCustomerColumn;

    @FXML
    private TableColumn<AppointmentTableRow, String> appointmentTypeColumn;

    @FXML
    private TableColumn<AppointmentTableRow, String> appointmentTimeColumn;

    @FXML
    private TableColumn<AppointmentTableRow, String> appointmentLengthColumn;
    
    @FXML
    private TableColumn<AppointmentTableRow, String> appointmentUserColumn;

    @FXML
    private TableView<CustomerTableRow> customerTable;

    @FXML
    private TableColumn<CustomerTableRow, String> customerNameColumn;
    
    @FXML
    private TableColumn<CustomerTableRow, String> customerPhoneColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerAddressColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerCityColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerActiveColumn;
    
    @FXML
    private TableColumn<CustomerTableRow, String> customerIdColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerAddressIdColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerAddress2Column;

    @FXML
    private TableColumn<CustomerTableRow, String> customerCityIdColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerCountryIdColumn;

    @FXML
    private TableColumn<CustomerTableRow, String> customerCountryColumn;
    
    @FXML
    private TableColumn<CustomerTableRow, String> customerPostalCodeColumn;
    
    @FXML
    void onActionToggleSortBy(ActionEvent event) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy - hh:mm a");
        
        // Sort by all
        if (this.sortByToggleGroup.getSelectedToggle().equals(this.sortByAllRb)) {
            appointmentTable.setItems(AppointmentTableRow.getAppointmentListAll());
        }
        
        // sort by week
        if (this.sortByToggleGroup.getSelectedToggle().equals(this.sortByWeekRb)) {
            // clear list from any previous calls
            AppointmentTableRow.clearAppointmentListWeek();
            
            for (AppointmentTableRow appointment : AppointmentTableRow.getAppointmentListAll()) {
                Date apptDate = sdf.parse(appointment.getStart().getValue());
                Date today = new Date();
                
                if (((apptDate.after(DateTimeLocale.getWeekStartDate()) && 
                        (apptDate.before(DateTimeLocale.getWeekEndDate())))) ||
                        (apptDate.equals(today))) {
                    AppointmentTableRow.getAppointmentListWeek().add(appointment);
                }
            }
            appointmentTable.setItems(AppointmentTableRow.getAppointmentListWeek());
        }
        
        if (this.sortByToggleGroup.getSelectedToggle().equals(this.SortByMonthRb)) {
            // clear list from any previous calls
            AppointmentTableRow.clearAppointmentListMonth();
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

            for (AppointmentTableRow appointment : AppointmentTableRow.getAppointmentListAll()) {
                Date apptDate = sdf.parse(appointment.getStart().getValue());
                LocalDate ld = apptDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (ld.getMonthValue() == currentMonth)
                    AppointmentTableRow.getAppointmentListMonth().add(appointment);
            }
            appointmentTable.setItems(AppointmentTableRow.getAppointmentListMonth());
        }
    }

    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws IOException {
        if (appointmentTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                String apptId = appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId().getValue();
                DatabaseReadWrite.deleteRecord("appointment", "appointmentId", Integer.parseInt(apptId));

                // reload menu
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws IOException {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                String customerId = customerTable.getSelectionModel().getSelectedItem().getCustomerId().getValue();
                DatabaseReadWrite.deleteRecord("appointment", "customerId", Integer.parseInt(customerId));
                DatabaseReadWrite.deleteCustomerAll(customerId);
                
                // reload menu
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionDisplayModifyAppointmentMenu(ActionEvent event) {
        modifyAppointmentFlag = true;
        try { 
            AppointmentsMenuController.initializeModifyAppointment(appointmentTable.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentsMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment to modify.");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionDisplayModifyCustomerMenu(ActionEvent event) {
        modifyCustomerFlag = true;
        try {
            CustomersMenuController.initializeModifyAppointment(customerTable.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/CustomersMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } 
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer to modify.");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionDisplayNewApppointmentMenu(ActionEvent event) throws IOException {
        newAppointmentFlag = true;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDisplayNewCustomerMenu(ActionEvent event) throws IOException {
        newCustomerFlag = true;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomersMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDisplayReportMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionExitProgram(ActionEvent event) throws Exception {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to quit?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            DBConnection.closeConnection();
            System.exit(0);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // clear tablerows from any previous calls
        AppointmentTableRow.clearAppointmentListAll();
        CustomerTableRow.clearCustomerList();
        
        ResultSet appointmentRs = DatabaseReadWrite.getAllRecords("appointment");
        ResultSet customerRs = DatabaseReadWrite.getAllRecords("customer");
        
        if (loginFlag) {
            try {
                // check for appt in 15 min
                appointmentRs.beforeFirst();
                
                while (appointmentRs.next()) {
                    Long apptDiff = DateTimeLocale.compareTime(appointmentRs.getString("start"));
                    
                    // if true, alert box stating appointment name and time until
                    if ((apptDiff >= 0) && (apptDiff <= 15)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Appointment within 15 minutes!");
                        alert.setContentText("There is an appointment with \n" +
                                AppointmentTableRow.getCustomerName(appointmentRs.getString("appointmentId")) +
                                " in " + apptDiff + " minutes.");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException | ParseException ex) {
                Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // reset loginFlag
        loginFlag = false;
        
        // hide ID columns from the user
        appointmentIdColumn.setVisible(false);
        customerIdColumn.setVisible(false);
        customerAddressIdColumn.setVisible(false);
        customerCityIdColumn.setVisible(false);
        customerCountryIdColumn.setVisible(false);
        
        // this entire block formats and sets up the table view
        
        // the first part of the lambda expression is the member of the tableview
        // we want to add data to. The second part pulls data from the TableRow object
        // we want to have displayed in the cell of that column. This is a quick way
        // of saying what data we want and where we want it.
        
        // appointment columns
        appointmentCustomerColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomer();
        });
        appointmentTypeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getType();
        });
        appointmentTimeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getStart();
        });
        appointmentLengthColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getEnd();
        });
        appointmentUserColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getUser();
        });
        appointmentIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getAppointmentId();
        });

        // customer columns
        customerIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerId();
        });
        customerAddressIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomeraddressId();
        });
        customerAddress2Column.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerAddress2();
        });
        customerPhoneColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerPhone();
        });
        customerCityIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerCityId();
        });
        customerCountryIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerCountryId();
        });
        customerCountryColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerCountry();
        });
        customerPostalCodeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerPostalCode();
        });
        customerNameColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerName();
        });
        customerAddressColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerAddress();
        });
        customerCityColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerCity();
        });
        customerActiveColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerActive();
        });
        
        try {
            // set cursor to top of result sets
            appointmentRs.beforeFirst();
            customerRs.beforeFirst();
            
            while (appointmentRs.next()) {
                // create and format appointment display variables
                String apptID = appointmentRs.getString("appointmentId");
                String custID = appointmentRs.getString("customerId");
                String custName = "";
                String type = appointmentRs.getString("type");
                String start = DateTimeLocale.utcToLocal(appointmentRs.getString("start"));
                String end = DateTimeLocale.appointmentLength(appointmentRs.getString("start"),
                        appointmentRs.getString("end"));
                String user = AppointmentTableRow.getAppointmentUser(appointmentRs.getString("appointmentId"));
                
                customerRs.beforeFirst();
                while (customerRs.next()) {
                    if (custID.equals(customerRs.getString("customerId")))
                        custName = customerRs.getString("customerName");
                }
                
                AppointmentTableRow tablerow = new AppointmentTableRow(
                        new ReadOnlyStringWrapper(custName),
                        new ReadOnlyStringWrapper(type),
                        new ReadOnlyStringWrapper(start),
                        new ReadOnlyStringWrapper(end),
                        new ReadOnlyStringWrapper(user),
                        new ReadOnlyStringWrapper(apptID)
                );
                AppointmentTableRow.getAppointmentListAll().add(tablerow);
            }
            
            customerRs.beforeFirst();
            while (customerRs.next()) {
                //create and format customer display variables
                String custId = customerRs.getString("customerId");
                String custName = customerRs.getString("customerName");
                String custAddressID = customerRs.getString("addressId");

                String[] addressInfo = CustomerTableRow.queryCustomerAddressAll(custAddressID);
                String custAddress = addressInfo[0];
                String custAddress2 = addressInfo[1];
                String postalCode = addressInfo[2];
                String custPhone = addressInfo[3];
                String custCityId = addressInfo[4];
                String custCity = addressInfo[5];
                String custCountryId = addressInfo[6];
                String custCountry = addressInfo[7];
                String custActive = customerRs.getString("active");
                
                //change Active from 0/1 to y/n
                if (custActive.equals("0"))
                    custActive = "NO";
                else
                    custActive = "YES";
                
                CustomerTableRow tablerow = new CustomerTableRow(
                        new ReadOnlyStringWrapper(custId),
                        new ReadOnlyStringWrapper(custName),
                        new ReadOnlyStringWrapper(custAddressID),
                        new ReadOnlyStringWrapper(custAddress),
                        new ReadOnlyStringWrapper(custAddress2),
                        new ReadOnlyStringWrapper(custPhone),
                        new ReadOnlyStringWrapper(postalCode),
                        new ReadOnlyStringWrapper(custCityId),
                        new ReadOnlyStringWrapper(custCity),
                        new ReadOnlyStringWrapper(custCountryId),
                        new ReadOnlyStringWrapper(custCountry),
                        new ReadOnlyStringWrapper(custActive)
                );
                CustomerTableRow.getCustomerList().add(tablerow);
            }

            appointmentTable.setItems(AppointmentTableRow.getAppointmentListAll());
            customerTable.setItems(CustomerTableRow.getCustomerList());
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // close the result sets
        try {
            appointmentRs.close();
            customerRs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}