package at.ac.tuwien.inso.sepm.ticketline.server.configuration;

import at.ac.tuwien.inso.sepm.ticketline.server.TicketlineServerApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = {TicketlineServerApplication.class, Jsr310JpaConverters.class})
public class JpaJsr310Configuration {
}
