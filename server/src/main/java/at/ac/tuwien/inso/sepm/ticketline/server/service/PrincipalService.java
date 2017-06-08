package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;

public interface PrincipalService {

    /**
     * Find a principal entry by the username
     *
     * @param username A string to search for in the username
     * @return The principal with the username
     */
    Principal findPrincipalByUsername(String username);
}
