package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface PrincipalService {

    /**
     * Find a principal entry by the username
     *
     * @param username A string to search for in the username
     * @return The principal with the username
     */
    Principal findPrincipalByUsername(String username);

    /**
     * fuzzy searches (paged) for principals
     *
     * @param query the search query
     * @param pageable the next requested page
     * @return list of principals
     */
    List<Principal> search(String query, Pageable pageable);

    /**
     * Enable/Disable the principal with the given id
     *
     * @param id The principal ID to unlock
     * @param enabled The enable status for the principal
     * @return The updated principal entry
     */
    Principal setEnabledForPrincipalWithId(UUID id, boolean enabled);

    /**
     * save or edit a rincipal
     *
     * @param principal The principal object to save or edit
     * @return the same principal passed into the method with fields updated
     */
    Principal save(Principal principal);

    /**
     * Reset the password of the Principal
     *
     * @param id The principal object to reset the password
     * @return The new principal entry with the updated (but encrypted password)
     */
    //will be done on the client side
    //Principal resetPassword(UUID id);
}
