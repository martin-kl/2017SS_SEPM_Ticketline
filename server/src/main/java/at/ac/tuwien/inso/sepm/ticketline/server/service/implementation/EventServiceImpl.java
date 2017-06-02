package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.EventSearch;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType.SEAT;
import static at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType.SECTOR;

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

        if(eventSearch.getEventName() != null && !eventSearch.getEventName().equals("")){
            builder.and(
                event.name.eq(eventSearch.getEventName())
            );
        }

        if(eventSearch.getDescription() != null && !eventSearch.getDescription().equals("")){
            builder.and(
                event.description.eq(eventSearch.getDescription())
            );
        }

        if(eventSearch.getArtistUUID() != null){
            builder.and(
                //TODO das is blödsinn - da muss noch das event berücksichtigt werden
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
        */
        for(Event e : page){
            filterPerformances(e, eventSearch);
        }

        return page.getContent();
    }

    private void filterPerformances(Event event, EventSearch eventSearch){
        Set<Performance> performances = event.getPerformances();

        //TODO is this really null?
        if(eventSearch.getPerformanceType() != null){
            if(eventSearch.getPerformanceType() == SEAT){
                performances = performances.stream().filter(p -> !(p.getLocation() instanceof SeatLocation))
                    .collect(Collectors.toSet());
            }
            if(eventSearch.getPerformanceType() == SECTOR){
                performances = performances.stream().filter(p -> !(p.getLocation() instanceof SectorLocation))
                    .collect(Collectors.toSet());
            }
        }

        if(eventSearch.getPerformanceDuration() != null){
            performances = performances.stream().filter(
                p -> performanceMatchesDuration(p, eventSearch.getPerformanceDuration()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceStartDate() != null && eventSearch.getPerformanceEndDate() != null){
            performances = performances.stream().filter(p ->
                    LocalDate.from(p.getStartTime()).equals(eventSearch.getPerformanceStartDate()) &&
                    LocalDate.from(p.getEndTime()).equals(eventSearch.getPerformanceEndDate()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceTicketPrice() != null){
            performances = performances.stream().filter(p->
                    performanceMatchesPrice(p, eventSearch.getPerformanceTicketPrice()))
                .collect(Collectors.toSet());
        }

        if(eventSearch.getPerformanceLocationUUID() != null){
            performances = performances.stream().filter(p->
                !p.getLocation().getId().equals(eventSearch.getPerformanceLocationUUID()))
                .collect(Collectors.toSet());
        }

        event.setPerformances(performances);
    }

    private boolean performanceMatchesPrice(Performance performance, BigDecimal price){
        boolean b1 = performance.getDefaultPrice().abs().compareTo(price.add(new BigDecimal(priceTolerance))) < 0;
        boolean b2 = performance.getDefaultPrice().abs().compareTo(price.add(new BigDecimal(-priceTolerance))) > 0;
        return b1 && b2;
    }

    //smaller than the given duration + 30 min
    //greater than the given duration - 30 min
    private boolean performanceMatchesDuration(Performance performance, Duration duration){
        boolean b1 = Duration.between(performance.getStartTime(),performance.getEndTime()).abs()
            .compareTo(duration.plusMinutes(durationToleranceInMinutes)) < 0;

        boolean b2 = Duration.between(performance.getStartTime(),performance.getEndTime()).abs()
            .compareTo(duration.minusMinutes(durationToleranceInMinutes)) > 0;

        return b1 && b2;
    }
}
