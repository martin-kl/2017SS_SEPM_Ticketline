package at.ac.tuwien.inso.sepm.ticketline.server.entity.base;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@EntityListeners({ AuditingEntityListener.class }) // http://www.baeldung.com/database-auditing-jpa
@MappedSuperclass
public abstract class Audited {

    @CreatedDate
    @Column
    @NotNull
    @Getter
    protected Instant createdAt;

    @LastModifiedDate
    @Column
    @NotNull
    @Getter
    protected Instant lastModifiedAt;

}
