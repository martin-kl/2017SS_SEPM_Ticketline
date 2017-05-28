package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(UUID id);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAllByOrderByPublishedAtDesc();


    /**
     * fins all news the user with uuid has not seen
     * @param uuid
     * @return
     */
    @Query("SELECT n FROM News n WHERE NOT EXISTS (SELECT pn FROM PrincipalNews pn WHERE pn.news = n AND pn.principal.id = ?1 AND pn.seen = true) ORDER BY n.publishedAt DESC")
    List<News> findAllNotSeenByUserWithId(UUID uuid);


}
