package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QPrincipal;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ConflictException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.DowngradeOwnAccountException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EMailAlreadyInUseException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.LockOwnAccountException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.ValidationHelper;
import com.querydsl.core.BooleanBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Slf4j
@Service
class PrincipalServiceImpl implements PrincipalService {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public List<Principal> findAll(Pageable pageable) {
        Pageable adaptedPageable = new PageRequest(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            new Sort("username")
        );
        Page<Principal> page = principalRepository.findAll(adaptedPageable);
        if (page == null) {
            return new ArrayList<>();
        }
        return page.getContent();
    }

    @Override
    public Principal findPrincipalByUsername(String username) {
        return principalRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Principal> search(String query, Boolean locked, Pageable pageable) {
        QPrincipal principal = QPrincipal.principal;
        BooleanBuilder builder = new BooleanBuilder();
        for (String token : query.split(" ")) {
            BooleanBuilder b = new BooleanBuilder();
            b.or(principal.username.containsIgnoreCase(token));
            b.or(principal.email.containsIgnoreCase(token));
            builder.and(b.getValue());
        }
        if (locked != null) {
            BooleanBuilder b = new BooleanBuilder();
            b.or(principal.enabled.eq(!locked));
            builder.and(b.getValue());
        }
        Page<Principal> page = principalRepository.findAll(builder.getValue(), pageable);
        if (page == null) {
            return new ArrayList<>();
        }
        return page.getContent();
    }

    @Override
    public Principal setEnabledForPrincipalWithId(UUID id, boolean locked) {
        //we have to negate locked to get enabled
        boolean enabled = !locked;
        //if we wanna set it to disable, check if it is not the current user, because the current
        //user should not be able to lock himself out
        if (!enabled) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
            Principal currentPrincipal = findPrincipalByUsername(user.getUsername());
            if (currentPrincipal.getId().equals(id)) {
                //this is not allowed
                throw new LockOwnAccountException(
                    "Conflict: The user with name \"" + currentPrincipal.getUsername()
                        + "\"wanted to lock himself out which is not possible");
            }
        }
        principalRepository.updateEnabledFlag(id, enabled);
        return principalRepository.findOne(id);
    }

    @Override
    public Principal save(Principal principal, String password) {
        //check the password length (if it is not null or "") here and not after the if else
        //because we have to encode it in there
        if (password != null && (!password.equals("")) && password.length() < 6) {
            log.error("Password is not even 6 character long, that is not valid");
            throw new BadRequestException(
                "Password is not even 6 character long, that is not valid");
        }

        if (principal.getId() == null) {
            //we have a new principal not an edit, so set loginCount to zero
            principal.setFailedLoginCount(0);
            principal.setEnabled(true);
            if (password == null || password.equals("")) {
                log.error("Wanted to save a new principal but password is empty");
                throw new BadRequestException("Password for new principal is empty");
            }
            //encode the plaintext password
            principal.setPassword(passwordEncoder.encode(password));
            if (checkIfUsernameExists(principal.getUsername())) {
                throw new ConflictException("Username is already in use");
            }
            if (checkIfEMailExists(principal.getEmail())) {
                throw new EMailAlreadyInUseException();
            }
        } else {
            //we edit a user
            Principal principal1FromRepo = principalRepository.findOne(principal.getId());

            //update failed login counts in case someone tried to login with the user in the meantime
            principal.setFailedLoginCount(principal1FromRepo.getFailedLoginCount());

            //if he wants to change the username we have to check if it is available
            if (!principal1FromRepo.getUsername().equals(principal.getUsername())) {
                if (checkIfUsernameExists(principal.getUsername())) {
                    throw new ConflictException("Username is already in use");
                }
            }
            if (!principal1FromRepo.getEmail().equals(principal.getEmail())) {
                if (checkIfEMailExists(principal.getEmail())) {
                    throw new EMailAlreadyInUseException();
                }
            }

            //look if he wants to set a new password
            if (password == null || password.equals("")) {
                //password has not changed - load old password and save it again
                principal.setPassword(principal1FromRepo.getPassword());
            } else {
                principal.setPassword(passwordEncoder.encode(password));
            }

            //an admin cannot set itself to the role of a seller
            if (principal.getRole() == Role.SELLER && principal1FromRepo.getRole() == Role.ADMIN) {
                User user = (User) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
                Principal currentPrincipal = findPrincipalByUsername(user.getUsername());
                if (currentPrincipal.getId() == principal.getId()) {
                    //this is not allowed
                    throw new DowngradeOwnAccountException(
                        "Conflict: The admin with username \"" + principal.getUsername()
                            + "\"wanted to set himself to the role of a SELLER, which is not possible");
                }
            }
        }
        try {
            return principalRepository.save(principal);
        } catch (TransactionSystemException e) {
            throw new BadRequestException(ValidationHelper.getErrorMessages(e).toString());
        }
    }

    private boolean checkIfEMailExists(String email) {
        email = email.toLowerCase();
        List<Principal> principalList = principalRepository.findAll();
        for (Principal principal : principalList) {
            if (principal.getEmail().toLowerCase().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfUsernameExists(String username) {
        username = username.toLowerCase();
        List<Principal> principalList = principalRepository.findAll();
        for (Principal principal : principalList) {
            if (principal.getUsername().toLowerCase().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
