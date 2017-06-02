package at.ac.tuwien.inso.sepm.ticketline.server.service.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
@EqualsAndHashCode
public class EventSearch {
    private String eventName;
    private String description;
    private PerformanceType performanceType;
    private Duration performanceDuration;
    private LocalDate performanceDate;
    private LocalDateTime performanceDateTime;
    private BigDecimal performanceTicketPrice;
    private UUID performanceLocationUUID;
    private UUID artistUUID;
}
