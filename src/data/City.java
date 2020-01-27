
package data;

/**
 *
 * @author Blake Thompson
 */
public class City extends DbEntry{
    private String cityId;
    private String city;
    private String countryId;
    
    public static String tableFields = "city, countryId, createDate, createdBy, lastUpdateBy";

    public City(String cityId, String city, String countryId, String createDate, String createdBy, 
            String lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
    }

    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}