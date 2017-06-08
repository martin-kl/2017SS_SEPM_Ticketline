package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QPrincipal;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ConflictException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.ValidationHelper;
import com.querydsl.core.BooleanBuilder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
class PrincipalServiceImpl implements PrincipalService {

    @Autowired
    private PrincipalRepository principalRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Principal findPrincipalByUsername(String username) {
        return principalRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Principal> search(String query, Pageable pageable) {
        QPrincipal principal = QPrincipal.principal;
        BooleanBuilder builder = new BooleanBuilder();
        for (String token : query.split(" ")) {
            BooleanBuilder b = new BooleanBuilder();
            b.or(principal.username.containsIgnoreCase(token));
            builder.and(b.getValue());
        }
        Page<Principal> page = principalRepository.findAll(builder.getValue(), pageable);
        if (page == null) {
            return new ArrayList<>();
        }
        return page.getContent();
    }

    @Override
    public Principal setEnabledForPrincipalWithId(UUID id, boolean enabled) {
        principalRepository.updateEnabledFlag(id, enabled);
        return principalRepository.findOne(id);
    }

    @Override
    public Principal save(Principal principal) {
        try {
            return principalRepository.save(principal);
        } catch (TransactionSystemException e) {
            throw new BadRequestException(ValidationHelper.getErrorMessages(e).toString());
        }
    }

    /*
    //reset password has to be done on the client side because we cannot encode the new password here and decode it on the client side

    @Override
    public Principal resetPassword(UUID id) {
        //RandomStringUtils randomStringUtils
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&*()-_=+[{]}\\|:\'\"<.>")
            .toCharArray();
        String randomStr = RandomStringUtils
            .random(10, 0, possibleCharacters.length - 1, false, false,
                possibleCharacters, new SecureRandom());

        System.out.println("\n\n\t\tgenerated password: \"" + randomStr + "\"\n\n");

        Principal principal = principalRepository.findOne(id);
        //principal.setPassword(passwordEncoder.encode(randomStr));
        principal.setPassword(randomStr);
        return save(principal);
    }
    */
}
