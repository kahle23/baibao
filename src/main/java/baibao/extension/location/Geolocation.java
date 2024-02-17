package baibao.extension.location;

import kunlun.data.geo.GeoCoordinate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The geographical location information.
 * @see <a href="https://en.wikipedia.org/wiki/Geolocation">Geolocation</a>
 * @author Kahle
 */
public class Geolocation extends Location implements GeoCoordinate, Serializable {
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

    @Override
    public BigDecimal getLongitude() {

        return longitude;
    }

    @Override
    public void setLongitude(BigDecimal longitude) {

        this.longitude = longitude;
    }

    @Override
    public BigDecimal getLatitude() {

        return latitude;
    }

    @Override
    public void setLatitude(BigDecimal latitude) {

        this.latitude = latitude;
    }

    @Override
    public BigDecimal getAltitude() {

        return altitude;
    }

    @Override
    public void setAltitude(BigDecimal altitude) {

        this.altitude = altitude;
    }

}
