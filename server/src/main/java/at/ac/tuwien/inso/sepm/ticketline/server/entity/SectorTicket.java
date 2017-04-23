package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="sector")
public class SectorTicket extends Ticket {

    // TODO
    // must be done like this till we have support for inheritence in the builder annotation
    // https://github.com/rzwitserloot/lombok/pull/1337
    @Builder
    public SectorTicket(
        UUID id,
        BigDecimal price,
        Performance performance,
        Set<TicketHistory> ticketHistorySet,
        Sector sector
    ) {
        super(id, price, performance, ticketHistorySet);
        this.sector = sector;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_ticket_sector")
    )
    private Sector sector;

}
