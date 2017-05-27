package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;

import java.util.List;
import java.util.UUID;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAll();


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return the news entry
     */
    News findOne(UUID id);

    /**
     * Publish a single news entry
     *
     * @param news to publish
     * @return published news entry
     */
    News publishNews(News news);


    /**
     * reports news as seen
     * @param newsId
     * @param principal
     * @return the news
     */
    News reportSeen(UUID newsId, Principal principal);

    /**
     *
     * @param userId
     * @return
     */
    List<News> findAllNotSeenByUser(UUID userId);
}
