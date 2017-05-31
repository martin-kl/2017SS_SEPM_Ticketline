package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;

public interface PrincipalService {

    Principal findPrincipalByUsername(String username);
}
