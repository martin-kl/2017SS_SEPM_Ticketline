package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.EventSearch;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType.SEAT;
import static at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType.SECTOR;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final int durationToleranceInMinutes = 30;
    private final double priceTolerance = 10.0;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> findAll(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        if (page == null) {
            return new ArrayList<>();
        }
        return page.getContent();
    }

    @Override
    public Event findOne(UUID id) {
        return eventRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Event> search(EventSearch eventSearch, Pageable pageable){
        QEvent event = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder();

        if(eventSearch.getEventName() != null){
            builder.and(
                event.name.containsIgnoreCase(eventSearch.getEventName())
            );
        }

        if(eventSearch.getDescription() != null){
            builder.and(
                event.description.containsIgnoreCase(eventSearch.getDescription())
            );
        }

        if(eventSearch.getEventCategory() != null){
            builder.and(
                event.category.stringValue().containsIgnoreCase(eventSearch.getEventCategory())
            );
        }

        if(eventSearch.getArtistUUID() != null){
            builder.and(
                event.eventArtists.any().artist.id.eq(eventSearch.getArtistUUID())
            );
        }

        Page<Event> page = eventRepository.findAll(builder.getValue(), pageable);
        if (page == null) {
            return new ArrayList<>();
        }

        /*
        * apply performanceFilter after eventFilter
        * this can be done at the service layer, because events usually
        * contain a small number of performances
        * events without performances are removed from the result
        */
        List<Event> eventsWithPerformances = new LinkedList<>();
        for(Event e : page.getContent()){
            filterPerformances(e, eventSearch);
            if(e.getPerformances().size() > 0){
                eventsWithPerformances.add(e);
            } else {
                log.info("Removed event " + e.getName() + ", because it has no matching performances");
            }
        }
        log.info("eventSearch: " + eventSearch);
        return eventsWithPerformances;
    }

    private void filterPerformances(Event event, EventSearch eventSearch){
        Set<Performance> performances = event.getPerformances();

        if(eventSearch.getPerformanceType() != null){
            if(eventSearch.getPerformanceType() == SEAT){
                performances = performances.stream().filter(p -> p.getLocation() instanceof SeatLocation)
                    .collect(Collectors.toSet());
            }
            if(eventSearch.getPerformanceType() == SECTOR){
                performances = performances.stream().filter(p -> p.getLocation() instanceof SectorLocation)
                    .collect(Collectors.toSet());
            }
        }

        if(eventSearch.getPerformanceDuration() != null){
            performances = performances.stream().filter(
                p -> performanceMatchesDuration(p, eventSearch.getPerformanceDuration()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceStartDate() != null){
            performances = performances.stream().filter(p ->
                    LocalDateTime.ofInstant(p.getStartTime(), ZoneOffset.systemDefault()).
                        toLocalDate().equals(eventSearch.getPerformanceStartDate()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceEndDate() != null){
            performances = performances.stream().filter(p ->
                LocalDateTime.ofInstant(p.getEndTime(), ZoneOffset.systemDefault()).
                    toLocalDate().equals(eventSearch.getPerformanceEndDate()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceTicketPrice() != null){
            performances = performances.stream().filter(p->
                    performanceMatchesPrice(p, eventSearch.getPerformanceTicketPrice()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceLocationUUID() != null){
            performances = performances.stream().filter(p->
                p.getLocation().getId().equals(eventSearch.getPerformanceLocationUUID()))
                .collect(Collectors.toSet());
        }

        event.setPerformances(performances);
    }

    private boolean performanceMatchesPrice(Performance performance, BigDecimal price){
        boolean b1 = performance.getDefaultPrice().abs().compareTo(price.add(new BigDecimal(priceTolerance))) < 0;
        boolean b2 = performance.getDefaultPrice().abs().compareTo(price.add(new BigDecimal(-priceTolerance))) > 0;
        return b1 && b2;
    }

    private boolean performanceMatchesDuration(Performance performance, Duration duration){
        boolean b1 = Duration.between(performance.getStartTime(),performance.getEndTime()).abs()
            .compareTo(duration.plusMinutes(durationToleranceInMinutes)) < 0;

        boolean b2 = Duration.between(performance.getStartTime(),performance.getEndTime()).abs()
            .compareTo(duration.minusMinutes(durationToleranceInMinutes)) > 0;

        if(!(b1 && b2)){
            log.debug("removed performance " + performance.getName() + " to event " + performance.getEvent().getName() +
            "because duration does not match");
            log.debug("exact duration: " + Duration.between(performance.getStartTime(), performance.getEndTime()).abs());
            log.debug("searched duration: " + duration);
        }

        return b1 && b2;
    }
}
