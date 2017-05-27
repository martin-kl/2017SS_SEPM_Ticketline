package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.PrincipalNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrincipalNewsRepository extends JpaRepository<PrincipalNews, UUID> {
    PrincipalNews findByNewsIdAndPrincipal(UUID newsId, Principal principal);
}
