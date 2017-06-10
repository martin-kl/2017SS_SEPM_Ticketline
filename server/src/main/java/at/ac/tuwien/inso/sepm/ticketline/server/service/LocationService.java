package at.ac.tuwien.inso.sepm.ticketline.server.service;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;

import java.util.List;

public interface LocationService {
    /**
     * Find events which match the attributes specified in eventSearch
     *
     * @param location contains the attributes which should be searched
     * @return list of locations matching the given search parameters
     * */
    List<Location> search(Location location);
}
