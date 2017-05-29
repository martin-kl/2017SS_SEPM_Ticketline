package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     * @return ordered list of al news entries
     */
    List<News> findAll(Pageable page);


    /**
     * Find a single news entry by id.
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
     * Reports a news entry as seen
     * @param newsId the id of the news entry to report as seen
     * @param principal the user that saw the news
     * @return the news entry
     */
    News reportSeen(UUID newsId, Principal principal);

    /**
     * Finds a list of all unseen news for the user
     * @param userId the user id for whom we wanna find all unseen news
     * @return a list of all unseen news for the User-Id
     */
    List<News> findAllNotSeenByUser(UUID userId, Pageable page);
}
