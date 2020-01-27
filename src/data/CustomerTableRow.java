
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
public class CustomerTableRow {
    private ObservableValue<String> customerId;
    private ObservableValue<String> customerName;
    private ObservableValue<String> customeraddressId;
    private ObservableValue<String> customerAddress;
    private ObservableValue<String> customerAddress2;
    private ObservableValue<String> customerPhone;
    private ObservableValue<String> customerPostalCode;
    private ObservableValue<String> customerCityId;
    private ObservableValue<String> customerCity;
    private ObservableValue<String> customerCountryId;
    private ObservableValue<String> customerCountry;
    private ObservableValue<String> customerActive;
    
    private static ObservableList<CustomerTableRow> customerList = FXCollections.observableArrayList();

    public CustomerTableRow(ObservableValue<String> customerId,
            ObservableValue<String> customerName, 
            ObservableValue<String> customeraddressId, 
            ObservableValue<String> customerAddress, 
            ObservableValue<String> customerAddress2, 
            ObservableValue<String> customerPhone,
            ObservableValue<String> customerPostalCode,
            ObservableValue<String> customerCityId, 
            ObservableValue<String> customerCity, 
            ObservableValue<String> customerCountryId, 
            ObservableValue<String> customerCountry, 
            ObservableValue<String> customerActive) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customeraddressId = customeraddressId;
        this.customerAddress = customerAddress;
        this.customerAddress2 = customerAddress2;
        this.customerPhone = customerPhone;
        this.customerPostalCode = customerPostalCode;
        this.customerCityId = customerCityId;
        this.customerCity = customerCity;
        this.customerCountryId = customerCountryId;
        this.customerCountry = customerCountry;
        this.customerActive = customerActive;
    }
    
    public static void clearCustomerList() {
        customerList.clear();
    }

    public ObservableValue<String> getCustomerPostalCode() {
        return customerPostalCode;
    }
    public void setCustomerPostalCode(ObservableValue<String> customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    public ObservableValue<String> getCustomerPhone() {
        return customerPhone;
    }
    public void setCustomerPhone(ObservableValue<String> customerPhone) {
        this.customerPhone = customerPhone;
    }

    public ObservableValue<String> getCustomeraddressId() {
        return customeraddressId;
    }
    public void setCustomeraddressId(ObservableValue<String> customeraddressId) {
        this.customeraddressId = customeraddressId;
    }

    public ObservableValue<String> getCustomerCityId() {
        return customerCityId;
    }
    public void setCustomerCityId(ObservableValue<String> customerCityId) {
        this.customerCityId = customerCityId;
    }

    public ObservableValue<String> getCustomerCountryId() {
        return customerCountryId;
    }
    public void setCustomerCountryId(ObservableValue<String> customerCountryId) {
        this.customerCountryId = customerCountryId;
    }
    
    public ObservableValue<String> getCustomerId() {
        return customerId;
    }
    public void setCustomerId(ObservableValue<String> customerId) {
        this.customerId = customerId;
    }

    public ObservableValue<String> getCustomerAddress2() {
        return customerAddress2;
    }
    public void setCustomerAddress2(ObservableValue<String> customerAddress2) {
        this.customerAddress2 = customerAddress2;
    }

    public ObservableValue<String> getCustomerCountry() {
        return customerCountry;
    }
    public void setCustomerCountry(ObservableValue<String> customerCountry) {
        this.customerCountry = customerCountry;
    }
    
    public static ObservableList<CustomerTableRow> getCustomerList() {
        return customerList;
    }
    public void setCustomerList(ObservableList<CustomerTableRow> customerList) {
        this.customerList = customerList;
    }

    public ObservableValue<String> getCustomerName() {
        return customerName;
    }
    public void setCustomerName(ObservableValue<String> customerName) {
        this.customerName = customerName;
    }

    public ObservableValue<String> getCustomerAddress() {
        return customerAddress;
    }
    public void setCustomerAddress(ObservableValue<String> customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ObservableValue<String> getCustomerCity() {
        return customerCity;
    }
    public void setCustomerCity(ObservableValue<String> customerCity) {
        this.customerCity = customerCity;
    }

    public ObservableValue<String> getCustomerActive() {
        return customerActive;
    }
    public void setCustomerActive(ObservableValue<String> customerActive) {
        this.customerActive = customerActive;
    }
    
    public static String[] queryCustomerAddressAll(String addressId) throws SQLException {
        String sqlStatement = "SELECT address.address, address.address2, address.phone, " +
                "address.postalCode, city.cityId, city.city, country.countryId, country.country " +
                "FROM customer JOIN address ON customer.addressId = address.addressId " +
                "JOIN city ON address.cityId = city.cityId JOIN country " +
                "ON city.countryId = country.countryId " +
                "WHERE customer.addressId = " + addressId;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        result.next();
        
        String[] addressInfo = {
            result.getString("address"),
            result.getString("address2"),
            result.getString("postalCode"),
            result.getString("phone"),
            result.getString("cityId"),
            result.getString("city"),
            result.getString("countryId"),
            result.getString("country")};
        return addressInfo;
    }

    public static String getCustomerCity(String customerId) throws SQLException {
        String sqlStatement = "SELECT city.city, country.country\n" +
                "FROM customer\n" +
                "JOIN address\n" +
                "ON customer.addressId = address.addressId\n" +
                "JOIN city\n" +
                "ON address.cityId = city.cityId\n" +
                "JOIN country\n" +
                "ON city.countryId = country.countryId\n" +
                "WHERE customerId = " + customerId;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        result.next();
        return result.getString("city") + ", " + result.getString("country");
    }
}