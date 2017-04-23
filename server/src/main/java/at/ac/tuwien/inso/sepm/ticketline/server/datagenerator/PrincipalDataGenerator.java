package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.PrincipalNews;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalNewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Profile("generateData")
@Component
public class PrincipalDataGenerator {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PrincipalNewsRepository principalNewsRepository;

    // injected so we make sure that news are loaded
    @Autowired
    private NewsDataGenerator newsDataGenerator;

    @PostConstruct
    private void generatePriceCategories() {
        if (principalRepository.count() > 0) {
            log.info("principals already generated");
        } else {

            List<News> newsList = newsRepository.findAll();

            Principal admin = Principal.builder()
                .role(Principal.Role.ADMIN)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .enabled(true)
                .build();

            Principal user = Principal.builder()
                .role(Principal.Role.SELLER)
                .username("user")
                .password(passwordEncoder.encode("password"))
                .enabled(true)
                .build();

            principalRepository.save(admin);
            principalRepository.save(user);

            // create principal news connections

            for (News news : newsList) {
                PrincipalNews principalNews = PrincipalNews.builder()
                    .news(news)
                    .principal(admin)
                    .seen(false)
                    .build();
                principalNewsRepository.save(principalNews);
            }

            for (News news : newsList) {
                PrincipalNews principalNews = PrincipalNews.builder()
                    .news(news)
                    .principal(user)
                    .seen(false)
                    .build();
                principalNewsRepository.save(principalNews);
            }

        }
    }


}
