package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;

import java.util.List;
import java.util.UUID;

public interface PrincipalService {

    List<PrincipalDTO> findAll(int page) throws ExceptionWithDialog;

    List<PrincipalDTO> search(String query, Boolean locked, int page) throws ExceptionWithDialog;

    PrincipalDTO save(PrincipalDTO principalDTO) throws ExceptionWithDialog;

    PrincipalDTO setLocked(UUID principalId, Boolean locked) throws ExceptionWithDialog;
}
