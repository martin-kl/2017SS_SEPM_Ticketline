package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.EventArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;

import javax.xml.crypto.Data;
import java.util.HashMap;
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
     * searches for top ten events matching the search parameters
     *
     * @param category the category or "" if category doesn't matter
     * @param monthsInPast the amount of months in the past to select events
     * @return a hashmap containing the top ten events and the amount of sold tickets
     */
    HashMap<Integer, EventDTO> searchTopTen(String category, Integer monthsInPast) throws DataAccessException;

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
     * @param searchParams the search params
     * @param page the page number to request
     * @return list of locations matching the search parameters
     */
    List<LocationDTO> searchLocations(LocationDTO searchParams, int page) throws DataAccessException;
}
