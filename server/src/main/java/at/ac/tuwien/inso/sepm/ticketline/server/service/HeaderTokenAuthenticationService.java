package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

public interface HeaderTokenAuthenticationService {

    /**
     * Authenticate user with username and password.
     *
     * @param username of the user
     * @param password of the user
     * @return an authentication token
     */
    AuthenticationToken authenticate(String username, CharSequence password);

    /**
     * Get informations about a header token.
     *
     * @param headerToken the authentication token for which the informations should be obtained
     * @return informations about the given token
     */
    AuthenticationTokenInfo authenticationTokenInfo(String headerToken);

    /**
     * Renew authentication based on a header token.
     *
     * @param headerToken current authentication token.
     * @return an authentication token
     */
    AuthenticationToken renewAuthentication(String headerToken);

    /**
     * Authenticate user with a header token.
     *
     * @param headerToken which should be user to authenticate user
     * @return Authenticated user
     * @throws AuthenticationException when the authentication of the provided headerToken fails
     */
    User authenticate(String headerToken) throws AuthenticationException;

}
