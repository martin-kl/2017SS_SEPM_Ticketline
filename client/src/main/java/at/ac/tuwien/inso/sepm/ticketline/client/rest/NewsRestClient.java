package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;

import java.util.List;
import java.util.UUID;

public interface NewsRestClient {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Find all unseen news for the current user.
     *
     * @return list of all unseen news for this user
     * @throws DataAccessException in case something went with the access to the data
     */
    List<SimpleNewsDTO> findAllUnseen() throws DataAccessException;

    /**
     * Returns the detailed news entry with the given news id.
     *
     * @param id The news id to search for.
     * @return The DetailedNewsDTO entry with the given news id.
     * @throws DataAccessException in case something went with the access to the data
     */
    DetailedNewsDTO findDetailedNews(UUID id) throws DataAccessException;
}
