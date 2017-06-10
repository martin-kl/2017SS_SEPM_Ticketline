package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.EventArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;

import java.util.HashMap;
import java.util.List;

public interface EventService {

    /**
     * Find all event entries.
     *
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findAll(int page) throws ExceptionWithDialog;


    /**
     * fuzzy searches for events
     *
     * @param searchParams the search params wrapped in EventSearchDTO
     * @param page the page number to request
     * @return list of events matching the search parameters
     */
    List<EventDTO> search(EventSearchDTO searchParams, int page) throws ExceptionWithDialog;

    /**
     * searches for top ten events matching the search parameters
     *
     * @param category the category or null if category doesn't matter
     * @param monthsInPast the amount of months in the past to select events
     * @return a hashmap containing the top ten events and the amount of sold tickets
     */
    HashMap<Integer, EventDTO> searchTopTen(EventCategory category, Integer monthsInPast) throws ExceptionWithDialog;

    /**
     * fuzzy searches for artists
     *
     * @param query the search params
     * @param page the page number to request
     * @return list of artists matching the search parameters
     */
    List<ArtistDTO> searchArtists(String query, int page) throws ExceptionWithDialog;

    /**
     * fuzzy searches for locations
     *
     * @param searchParams the search params
     * @param page the page number to request
     * @return list of locations matching the search parameters
     */
    List<LocationDTO> searchLocations(LocationDTO searchParams, int page) throws ExceptionWithDialog;
}
