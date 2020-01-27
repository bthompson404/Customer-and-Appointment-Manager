
package data;

/**
 *
 * @author Blake Thompson
 */
public class Address extends DbEntry{
    private String addressId;
    private String address;
    private String address2;
    private String cityId;
    private String postalCode;
    private String phone;
    
    public static String tableFields = "address, address2, cityId, postalCode,"
            + " phone, createDate, createdBy, lastUpdateBy";

    public Address(String addressId, String address, String address2, String cityId,
            String postalCode, String phone, String createDate, String createdBy,
            String lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}