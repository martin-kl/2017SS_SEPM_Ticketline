package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll(int page) throws DataAccessException;

    /**
     * find all news not seen by user
     * @return
     * @throws DataAccessException
     */
    List<SimpleNewsDTO> findAllUnseen(int page) throws DataAccessException;

    /**
     * publish news
     * @param news
     * @return
     * @throws DataAccessException
     */
    DetailedNewsDTO publish(DetailedNewsDTO news) throws DataAccessException;

}
