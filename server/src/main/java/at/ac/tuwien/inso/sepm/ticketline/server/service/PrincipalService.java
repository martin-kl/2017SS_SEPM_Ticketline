package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface PrincipalService {
    /**
     * Find all principals
     *
     * @param pageable The next requested page
     * @return list of principals (ordered by username)
     */
    List<Principal> findAll(Pageable pageable);

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
     * @param locked search in the locked status, if it is null, locked and unlocked principals are searched
     * @param pageable the next requested page
     * @return list of principals
     */
    List<Principal> search(String query, Boolean locked, Pageable pageable);

    /**
     * Enable/Disable the principal with the given id
     *
     * @param id The principal ID to unlock
     * @param locked The locked status for the principal
     * @return The updated principal entry
     */
    Principal setEnabledForPrincipalWithId(UUID id, boolean locked);

    /**
     * save or edit a rincipal
     *
     * @param principal The principal object to save or edit
     * @param password The new (or possible empty, in the case that the password has not changed) password
     * @return the same principal passed into the method with fields updated
     */
    Principal save(Principal principal, String password);

    /**
     * Reset the password of the Principal
     *
     * @param id The principal object to reset the password
     * @return The new principal entry with the updated (but encrypted password)
     */
    //will be done on the client side
    //Principal resetPassword(UUID id);
}
