package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, UUID> {
}
