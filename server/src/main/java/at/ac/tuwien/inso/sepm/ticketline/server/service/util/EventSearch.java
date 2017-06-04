package at.ac.tuwien.inso.sepm.ticketline.server.service.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType;
import lombok.*;
import org.apache.tomcat.jni.Local;
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
    private EventCategory eventCategory;
    private PerformanceType performanceType;
    private Duration performanceDuration;
    private LocalDate performanceStartDate;
    private LocalDate performanceEndDate;
    private BigDecimal performanceTicketPrice;
    private UUID performanceLocationUUID;
    private UUID artistUUID;
}
