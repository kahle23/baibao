package misaka.location;

import java.util.List;

public interface LocationProvider {

    Location info(String nameOrCode);

    List<Location> search(String nameOrCode);

    /**
     * The next level of the current location.
     * @param nameOrCode The name or code of the location
     * @param paramType The type of the current location
     *                  (Such as country, region, city, district, street)
     * @return A list of locations at the next level
     */
    List<Location> subLocations(String nameOrCode, String paramType);

}
