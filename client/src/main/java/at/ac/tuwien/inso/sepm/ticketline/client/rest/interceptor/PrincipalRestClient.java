package at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;

import java.util.List;
import java.util.UUID;

public interface PrincipalRestClient {

    List<PrincipalDTO> findAll(int page) throws DataAccessException;

    List<PrincipalDTO> search(String query, Boolean locked, int page) throws DataAccessException;

    PrincipalDTO save(PrincipalDTO principalDTO) throws ValidationException, DataAccessException;

    PrincipalDTO setLocked(UUID principalId, Boolean locked) throws DataAccessException;
}
