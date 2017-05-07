package at.ac.tuwien.inso.sepm.ticketline.server.service.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class TicketWrapper {
    private Ticket ticket;
    private TicketStatus status;
}
