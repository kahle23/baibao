package baibao.extension.location;

import artoria.data.geo.GeoCoordinate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The geographical location information.
 * @see <a href="https://en.wikipedia.org/wiki/Geolocation">Geolocation</a>
 * @author Kahle
 */
public class Geolocation extends Location implements Serializable {
    /**
     * The longitude.
     */
    private BigDecimal longitude;
    /**
     * The latitude.
     */
    private BigDecimal latitude;
    /**
     * The altitude.
     */
    private BigDecimal altitude;

    public BigDecimal getLongitude() {

        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {

        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {

        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {

        this.latitude = latitude;
    }

    public BigDecimal getAltitude() {

        return altitude;
    }

    public void setAltitude(BigDecimal altitude) {

        this.altitude = altitude;
    }

}
