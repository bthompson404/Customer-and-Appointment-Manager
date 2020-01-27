
package implement;

import data.*;
import java.sql.ResultSet;
import utilities.DBConnection;
import utilities.Query;

/**
 *
 * @author Blake Thompson
 */
public class DatabaseReadWrite {
    // get all records from a single table
    public static ResultSet getAllRecords(String tableName) {
        String sqlStatement = "SELECT * FROM " + DBConnection.databaseName + "."
                + tableName + ";";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        
        return result;
    }
    
    // universal record delete
    public static void deleteRecord(String tableName, String conditionKey, int conditionValue) {
        String sqlStatement = "DELETE FROM " + tableName +
                " WHERE " + conditionKey + " = " + conditionValue;
        Query.makeQuery(sqlStatement);
    }
    
    // insert and update address functions
    public static void insertAddress(Address address) {
        String sqlStatement = "INSERT INTO address (" +
                Address.tableFields +
                ") VALUES ('" + 
                address.getAddress() + "', '" +
                address.getAddress2() + "', " +
                address.getCityId() + ", '" +
                address.getPostalCode() + "', '" +
                address.getPhone() + "', '" +
                address.getCreateDate() + "', '" +
                address.getCreatedBy() + "', '" +
                address.getLastUpdateBy() + "')";
        Query.makeQuery(sqlStatement);
    }
    
    public static void updateAddress(Address address) {
        String sqlStatement = "UPDATE address SET " +
                "address = '" + address.getAddress() + "', " +
                "address2 = '" + address.getAddress2() + "', " +
                "cityId = " + address.getCityId() + ", " +
                "postalCode = '" + address.getPostalCode() + "', " +
                "phone = '" + address.getPhone() + "', " +
                "lastUpdateBy = '" + address.getLastUpdateBy() +
                "' WHERE addressId = " + address.getAddressId();
        Query.makeQuery(sqlStatement);
    }
    
    // insert and update appointment functions
    public static void insertAppointment(Appointment appointment) {
        String sqlStatement = "INSERT INTO appointment (" +
                Appointment.tableFields +
                ") VALUES (" +
                appointment.getCustomerId() + ", " +
                appointment.getUserId() + ", '" +
                appointment.getTitle() + "', '" +
                appointment.getDescription() + "', '" +
                appointment.getLocation() + "', '" +
                appointment.getContact() + "', '" +
                appointment.getType() + "', '" +
                appointment.getUrl() + "', '" +
                appointment.getStart() + "', '" +
                appointment.getEnd() + "', '" +
                appointment.getCreateDate() + "', '" +
                appointment.getCreatedBy() + "', '" +
                appointment.getLastUpdateBy() + "')";
        Query.makeQuery(sqlStatement);
    }
    
    public static void updateAppointment(Appointment appointment) {
        String sqlStatement = "UPDATE appointment SET " +
                "customerId = " + appointment.getCustomerId() + ", " +
                "userId = " + appointment.getUserId() + ", " +
                "type = '" + appointment.getType() + "', " +
                "start = '" + appointment.getStart() + "', " +
                "end = '" + appointment.getEnd() + "', " +
                "lastUpdateBy = '" + appointment.getUserId() + "' " +
                "WHERE appointmentId = " + appointment.getAppointmentId();
        Query.makeQuery(sqlStatement);
    }
    
    // insert and update city functions
    public static void insertCity(City city) {
        String sqlStatement = "INSERT INTO city (" +
                City.tableFields +
                ") VALUES ('" +
                city.getCity() + "', " +
                city.getCountryId() + ", '" +
                city.getCreateDate() + "', '" +
                city.getCreatedBy() + "', '" +
                city.getLastUpdateBy() + "')";
        Query.makeQuery(sqlStatement);
    }
    
    public static void updateCity(City city) {
        String sqlStatement = "UPDATE city SET " +
                "city = '" + city.getCity() + "', " +
                "countryId = " + city.getCountryId() + ", " +
                "lastUpdateBy = '" + city.getLastUpdateBy() +
                "' WHERE cityId = " + city.getCityId();
        Query.makeQuery(sqlStatement);
    }
    
    // insert and update country functions
    public static void insertCountry(Country country) {
        String sqlStatement = "INSERT INTO country (" +
                Country.tableFields +
                ") VALUES ('" +
                country.getCountry() + "', '" +
                country.getCreateDate() + "', '" +
                country.getCreatedBy() + "', '" +
                country.getLastUpdateBy() + "')";
        Query.makeQuery(sqlStatement);
    }
    
    public static void updateCountry(Country country) {
        String sqlStatement = "UPDATE country SET " +
                "country = '" + country.getCountry() + "', " +
                "lastUpdateBy = '" + country.getLastUpdateBy() + "' " +
                "WHERE countryId = " + country.getCountryId();
        Query.makeQuery(sqlStatement);
    }
    
    // insert and update customer functions
    public static void insertCustomer(Customer customer) {
        String sqlStatement = "INSERT INTO customer (" +
                Customer.tableFields +
                ") VALUES ('" +
                customer.getCustomerName() + "', " +
                customer.getAddressId() + ", " +
                customer.getActive() + ", '" +
                customer.getCreateDate() + "', '" +
                customer.getCreatedBy() + "', '" +
                customer.getLastUpdateBy() + "')";
        Query.makeQuery(sqlStatement);
    }
    
    public static void updateCustomer(Customer customer) {
        String sqlStatement = "UPDATE customer SET " +
                "customerName = '" + customer.getCustomerName() + "', " +
                "addressId = " + customer.getAddressId() + ", " +
                "active = " + customer.getActive() + ", " +
                "lastUpdateBy = '" + customer.getLastUpdateBy() +
                "' WHERE customerId = " + customer.getCustomerId();
        Query.makeQuery(sqlStatement);
    }
    
    // deletes customer as well as all related records
    public static void deleteCustomerAll(String customerId) {
        String sqlStatement = "DELETE customer, address, city, country FROM customer " +
                "INNER JOIN address ON customer.addressId = address.addressId " +
                "INNER JOIN city ON address.cityId = city.cityId " +
                "INNER JOIN country on city.countryId = country.countryId " +
                "WHERE customer.customerId = " + customerId;
        Query.makeQuery(sqlStatement);
    }
    
    // insert and update user functions
    public static void insertUser(User user) {
        String sqlStatement = "INSERT INTO user (" +
                user.tableFields +
                ") VALUES ('" +
                user.getUserName() + "', '" +
                user.getPassword() + "', " +
                user.getActive() + ", '" +
                user.getCreateDate() + "', '" +
                user.getCreatedBy() + "', '" +
                user.getLastUpdateBy() + "')";
        Query.makeQuery(sqlStatement);
    }
    
    public static void updateUser(User user) {
        String sqlStatement = "UPDATE user SET " +
                "userName = '" + user.getUserName() + "', " +
                "password = '" + user.getPassword() + "', " +
                "active = " + user.getActive() + ", " +
                "createDate = '" + user.getCreateDate() + "', " +
                "createdBy = '" + user.getCreatedBy() + "', " +
                "lastUpdate = '" + user.getLastUpdate() + "', " +
                "lastUpdateBy = '" + user.getLastUpdateBy() +
                "' WHERE userId = " + user.getUserId();
        Query.makeQuery(sqlStatement);
    }
    
    public static ResultSet getUserSchedule(String userId) {
        String sqlStatement = "SELECT appointmentId, start, end, userName FROM appointment " +
                "JOIN user ON appointment.userId = user.userId " +
                "WHERE appointment.userId = " + userId;
        Query.makeQuery(sqlStatement);
        return Query.getResult();
    }
}