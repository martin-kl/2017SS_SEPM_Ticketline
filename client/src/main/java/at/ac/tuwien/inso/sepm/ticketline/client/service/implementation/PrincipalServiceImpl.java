package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor.PrincipalRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PrincipalService;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PrincipalServiceImpl implements PrincipalService {

    PrincipalRestClient principalRestClient;

    PrincipalServiceImpl(PrincipalRestClient principalRestClient) {
        this.principalRestClient = principalRestClient;
    }

    /**
     * finds all principals
     * @param page
     * @return
     * @throws ExceptionWithDialog
     */
    @Override
    public List<PrincipalDTO> findAll(int page) throws ExceptionWithDialog {
        return principalRestClient.findAll(page);
    }

    /**
     * searches for principal dto
     * @param query will fuzzy search in all text fields of principal
     * @param locked true/false or null if both are to be searched
     * @param page
     * @return list of found principals
     * @throws ExceptionWithDialog
     */
    @Override
    public List<PrincipalDTO> search(String query, Boolean locked, int page) throws ExceptionWithDialog {
        return principalRestClient.search(query, locked, page);
    }

    /**
     * saves a principalDTO
     * @param principalDTO
     * @return the saved principal
     * @throws ExceptionWithDialog
     */
    @Override public PrincipalDTO save(PrincipalDTO principalDTO) throws ExceptionWithDialog {
        if (principalDTO.getUsername().length() < 3) throw new ValidationException("principal.validation.username");
        EmailValidator ev = new EmailValidator();
        if(principalDTO.getEmail() == null || principalDTO.getEmail().equals("") || !ev.isValid(principalDTO.getEmail(), null)) throw new ValidationException("customer.error.email");
        if(principalDTO.getId() == null) {
            if (principalDTO.getNewPassword().length()<6) {
                throw new ValidationException("principal.validation.password");
            }
        }
        return principalRestClient.save(principalDTO);
    }

    /**
     * sets principal with principalId to value of locked
     * @param principalId
     * @param locked
     * @return the principal
     * @throws ExceptionWithDialog
     */
    @Override
    public PrincipalDTO setLocked(UUID principalId, Boolean locked) throws ExceptionWithDialog {
        return principalRestClient.setLocked(principalId, locked);
    }
}
