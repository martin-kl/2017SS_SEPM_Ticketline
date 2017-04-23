package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("generateData")
@Component
public class PrincipalDataGenerator {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void generatePriceCategories() {
        if (principalRepository.count() > 0) {
            log.info("principals already generated");
        } else {

            Principal admin = Principal.builder()
                .role(Principal.Role.ADMIN)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .build();

            Principal user = Principal.builder()
                .role(Principal.Role.SELLER)
                .username("user")
                .password(passwordEncoder.encode("password"))
                .build();

            principalRepository.save(admin);
            principalRepository.save(user);

        }
    }


}
