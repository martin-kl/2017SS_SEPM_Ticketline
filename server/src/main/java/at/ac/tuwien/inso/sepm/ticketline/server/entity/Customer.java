
package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import java.time.LocalDate;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"ticketTransactions"})
@ToString(exclude = {"ticketTransactions"})
@Entity
public class Customer extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false, length = 10_000)
    @Length(min = 1)
    private String firstName;

    @Column(nullable = false, length = 10_000)
    @Length(min = 1)
    private String lastName;

    @Column(nullable = false, length = 10_000, unique = true)
    @Email
    @Length(min = 3)
    private String email;

    @Column(nullable = false, length = 10_000)
    @Length(min = 5)
    private String address;

    @Column(nullable = false)
    private LocalDate birthday;

    @Getter
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<TicketTransaction> ticketTransactions;
}
