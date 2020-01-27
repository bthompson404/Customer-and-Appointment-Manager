
package data;

/**
 *
 * @author Blake Thompson
 */
public class Customer extends DbEntry{
    private String customerId;
    private String customerName;
    private String addressId;
    private String active;
    
    public static String tableFields = "customerName, addressId, active, createDate, createdBy, lastUpdateBy";

    public Customer(String customerId, String customerName, String addressId, String active,
            String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getActive() {
        return active;
    }
    public void setActive(String active) {
        this.active = active;
    }

    public static String getTableFields() {
        return tableFields;
    }
    public static void setTableFields(String tableFields) {
        Customer.tableFields = tableFields;
    }
}