
package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import java.time.LocalDate;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"ticketHistories"})
@ToString(exclude = {"ticketHistories"})
@Entity
public class Customer extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false, length = 10_000)
    private String firstName;

    @Column(nullable = false, length = 10_000)
    private String lastName;

    @Column(nullable = false, length = 10_000)
    private String email;

    @Column(nullable = false, length = 10_000)
    private String address;

    @Column(nullable = false)
    private LocalDate birthday;

    @Getter
    @OneToMany(mappedBy = "customer")
    private Set<TicketHistory> ticketHistories;
}
