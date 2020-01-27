
package data;

/**
 *
 * @author Blake Thompson
 */
public class Country extends DbEntry{
    private String countryId;
    private String country;
    
    public static String tableFields = "country, createDate, createdBy, lastUpdateBy";

    public Country(String countryId, String country, String createDate, String createdBy,
            String lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.countryId = countryId;
        this.country = country;
    }

    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}