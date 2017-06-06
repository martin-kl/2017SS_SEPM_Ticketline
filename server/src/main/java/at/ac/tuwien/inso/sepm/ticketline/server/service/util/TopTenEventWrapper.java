package at.ac.tuwien.inso.sepm.ticketline.server.service.util;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
@EqualsAndHashCode
public class TopTenEventWrapper {
    private Event event;
    private long soldCount;

    public TopTenEventWrapper(Event event, long soldCount) {
        this.event = event;
        this.soldCount = soldCount;
    }
}
