package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
public class Customer extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false, length = 10_000)
    private String name;

    @Getter
    @OneToMany(mappedBy = "customer")
    private Set<TicketHistory> ticketHistories;
}
