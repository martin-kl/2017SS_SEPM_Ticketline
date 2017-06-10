package at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;

import java.util.List;
import java.util.UUID;

public interface PrincipalRestClient {

    /**
     * finds all principals
     * @param page
     * @return
     * @throws ExceptionWithDialog
     */
    List<PrincipalDTO> findAll(int page) throws DataAccessException;

    /**
     * searches for principal dto
     * @param query will fuzzy search in all text fields of principal
     * @param locked true/false or null if both are to be searched
     * @param page
     * @return list of found principals
     * @throws ExceptionWithDialog
     */
    List<PrincipalDTO> search(String query, Boolean locked, int page) throws ExceptionWithDialog;

    /**
     * saves a principalDTO
     * @param principalDTO
     * @return the saved principal
     * @throws ExceptionWithDialog
     */
    PrincipalDTO save(PrincipalDTO principalDTO) throws ExceptionWithDialog;

    /**
     * sets principal with principalId to value of locked
     * @param principalId
     * @param locked
     * @return the principal
     * @throws ExceptionWithDialog
     */
    PrincipalDTO setLocked(UUID principalId, Boolean locked) throws ExceptionWithDialog;
}

