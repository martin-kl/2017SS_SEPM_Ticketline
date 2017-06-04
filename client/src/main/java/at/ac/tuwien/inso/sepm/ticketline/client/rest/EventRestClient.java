package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.EventArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;

import java.util.List;

public interface EventRestClient {
    /**
     * Find all event entries.
     *
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findAll(int page) throws DataAccessException;


    /**
     * fuzzy searches for events
     *
     * @param searchParams the search params wrapped in EventSearchDTO
     * @param page the page number to request
     * @return list of events matching the search parameters
     */
    List<EventDTO> search(EventSearchDTO searchParams, int page) throws DataAccessException;

    /**
     * fuzzy searches for artists
     *
     * @param query the search params
     * @param page the page number to request
     * @return list of artists matching the search parameters
     */
    List<ArtistDTO> searchArtists(String query, int page) throws DataAccessException;

    /**
     * fuzzy searches for locations
     *
     * @param query the search params
     * @param page the page number to request
     * @return list of locations matching the search parameters
     */
    List<LocationDTO> searchLocations(String query, int page) throws DataAccessException;
}
