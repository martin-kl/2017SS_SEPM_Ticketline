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

    @Override
    public List<PrincipalDTO> findAll(int page) throws ExceptionWithDialog {
        return principalRestClient.findAll(page);
    }

    @Override
    public List<PrincipalDTO> search(String query, Boolean locked, int page) throws ExceptionWithDialog {
        return principalRestClient.search(query, locked, page);
    }

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

    @Override
    public PrincipalDTO setLocked(UUID principalId, Boolean locked) throws ExceptionWithDialog {
        return principalRestClient.setLocked(principalId, locked);
    }
}
