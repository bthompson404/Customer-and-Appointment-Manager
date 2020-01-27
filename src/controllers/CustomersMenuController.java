
package controllers;

import data.*;
import implement.DatabaseReadWrite;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import static utilities.DBConnection.conn;
import utilities.DateTimeLocale;

/**
 *
 * @author Blake Thompson
 */
public class CustomersMenuController implements Initializable {
    Stage stage;
    Parent scene;
    
    private static CustomerTableRow custModify;
    
    @FXML
    private Label customerVariableLabel;

    @FXML
    private TextField customerNameText;
    
    @FXML
    private TextField phonePrefixText;

    @FXML
    private TextField phoneSuffixText;

    @FXML
    private TextField customerAddressText;

    @FXML
    private TextField CustomerAddress2Text;

    @FXML
    private TextField customerCityText;

    @FXML
    private TextField customerCountryText;

    @FXML
    private TextField CustomerPostalCodeText;

    @FXML
    private RadioButton yesActiveRbtn;

    @FXML
    private RadioButton noActiveRbtn;

    @FXML
    void OnActionDisplayMainMenu(ActionEvent event) throws IOException {
        MainMenuController.newCustomerFlag = false;
        MainMenuController.modifyCustomerFlag = false;
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void OnActionSaveCustomer(ActionEvent event) throws IOException {
        // get all input fields
        String custCountry = customerCountryText.getText();
        String custCity = customerCityText.getText();
        String custAddress = customerAddressText.getText();
        String address2 = CustomerAddress2Text.getText();
        String postalCode = CustomerPostalCodeText.getText();
        String phonePrefix = phonePrefixText.getText();
        String phoneSuffix = phoneSuffixText.getText();
        String phone = phonePrefix + "-" + phoneSuffix;
        String customerName = customerNameText.getText();
        String lastUpdateBy = LoginMenuController.currentUser;
        String active = "0";
        if (yesActiveRbtn.isSelected())
            active = "1";
  
        boolean isValid = true;
        
        // test there are no blank fields
        String[] textFields = {customerName, phonePrefix, phoneSuffix, custAddress, custCity, custCountry, postalCode};
        for (String textField : textFields) {
            if (textField.equals("")) {
                isValid = false;
                break;
            }
        }
        
        if (isValid) {
            // branch for modify customer save
            if (MainMenuController.modifyCustomerFlag) {
                // get IDs from selected customer tablerow object
                String countryId = custModify.getCustomerCountryId().getValue();
                String cityId = custModify.getCustomerCityId().getValue();
                String addressId = custModify.getCustomeraddressId().getValue();
                String customerId = custModify.getCustomerId().getValue();

                // create new data objects and write to DB
                Country country = new Country(countryId, custCountry, "", "", "", lastUpdateBy);
                DatabaseReadWrite.updateCountry(country);

                City city = new City(cityId, custCity, countryId, "", "", "", lastUpdateBy);
                DatabaseReadWrite.updateCity(city);

                Address address = new Address(addressId, custAddress, address2, cityId,
                    postalCode, phone, "", "", "", lastUpdateBy);
                DatabaseReadWrite.updateAddress(address);

                Customer customer = new Customer(customerId, customerName, addressId, active,
                        "", "", "", lastUpdateBy);
                DatabaseReadWrite.updateCustomer(customer);

                MainMenuController.modifyCustomerFlag = false;
            }

            // branch for a new customer save
            else if (MainMenuController.newCustomerFlag) {
                int countryId = 0;
                int cityId = 0;
                int addressId = 0;
                String createdBy = LoginMenuController.currentUser;
                String createDate = DateTimeLocale.getCurrentUTC();

                // insert country and return generated ID for next insert
                String sqlStatement = "INSERT INTO country (country, createDate, createdBy, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, custCountry);
                    stmt.setString(2, createDate);
                    stmt.setString(3, createdBy);
                    stmt.setString(4, lastUpdateBy);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 1) {
                        ResultSet rs = stmt.getGeneratedKeys();
                        if(rs.next())
                            countryId = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                // insert city and return generated ID for next insert
                sqlStatement = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, custCity);
                    stmt.setInt(2, countryId);
                    stmt.setString(3, createDate);
                    stmt.setString(4, createdBy);
                    stmt.setString(5, lastUpdateBy);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 1) {
                        ResultSet rs = stmt.getGeneratedKeys();
                        if(rs.next())
                            cityId = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                // insert address and return ID for next insert
                sqlStatement = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, custAddress);
                    stmt.setString(2, address2);
                    stmt.setInt(3, cityId);
                    stmt.setString(4, postalCode);
                    stmt.setString(5, phone);
                    stmt.setString(6, createDate);
                    stmt.setString(7, createdBy);
                    stmt.setString(8, lastUpdateBy);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 1) {
                        ResultSet rs = stmt.getGeneratedKeys();
                        if(rs.next())
                            addressId = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                // insert customer ID
                sqlStatement = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sqlStatement);
                    stmt.setString(1, customerName);
                    stmt.setInt(2, addressId);
                    stmt.setString(3, active);
                    stmt.setString(4, createDate);
                    stmt.setString(5, createdBy);
                    stmt.setString(6, lastUpdateBy);

                    int rowsAffected = stmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());    
                }
            }

            MainMenuController.newCustomerFlag = false;
            MainMenuController.modifyCustomerFlag = false;

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill out all fields.");
            alert.showAndWait();
        }
    }
    
    public static void initializeModifyAppointment(CustomerTableRow customer) {
        custModify = customer;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // allow only numbers for postal code and phone number
        phonePrefixText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                phonePrefixText.setText(oldValue);
            }
            if (newValue.length() > 3) {
                phonePrefixText.setText(oldValue);
            }
        });
        phoneSuffixText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                phoneSuffixText.setText(oldValue);
            }
            if (newValue.length() > 4) {
                phoneSuffixText.setText(oldValue);
            }
        });
        CustomerPostalCodeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                CustomerPostalCodeText.setText(oldValue);
            }
            if (newValue.length() > 5) {
                CustomerPostalCodeText.setText(oldValue);
            }
        });
        // set new or modify menu depending on flag
        if (MainMenuController.newCustomerFlag)
            customerVariableLabel.setText("New Customer");
        else if (MainMenuController.modifyCustomerFlag)
            customerVariableLabel.setText("Modify Customer");
            
            customerNameText.setText(custModify.getCustomerName().getValue());
            phonePrefixText.setText(custModify.getCustomerPhone().getValue().substring(0, 3));
            phoneSuffixText.setText(custModify.getCustomerPhone().getValue().substring(4));
            customerAddressText.setText(custModify.getCustomerAddress().getValue());
            CustomerAddress2Text.setText(custModify.getCustomerAddress2().getValue());
            customerCityText.setText(custModify.getCustomerCity().getValue());
            customerCountryText.setText(custModify.getCustomerCountry().getValue());
            CustomerPostalCodeText.setText(custModify.getCustomerPostalCode().getValue());
            if (custModify.getCustomerActive().getValue().equals("No"))
                noActiveRbtn.setSelected(true);
    }
}