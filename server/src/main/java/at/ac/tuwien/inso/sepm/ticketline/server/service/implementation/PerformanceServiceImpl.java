package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.TicketWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class PerformanceServiceImpl implements PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final TicketRepository ticketRepository;
    private final TicketTransactionRepository ticketTransactionRepository;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository,
                                  TicketRepository ticketRepository,
                                  TicketTransactionRepository ticketTransactionRepository){
        this.performanceRepository = performanceRepository;
        this.ticketRepository = ticketRepository;
        this.ticketTransactionRepository = ticketTransactionRepository;
    }

    @Override
    public Performance findOne(UUID id) {
        return performanceRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Ticket> findAllTicketsToPerformanceID(UUID performanceID) {
        List<Ticket> ticketsToPerformance = ticketRepository.findAll();
        ticketsToPerformance.removeIf(t -> !t.getPerformance().getId().equals(performanceID));
        return ticketsToPerformance;
    }

    @Override
    public List<TicketWrapper> addStatusToTickets(List<Ticket> tickets){
        List<TicketWrapper> ticketsWithStatus = new ArrayList<>(tickets.size());

        for(Ticket ticket : tickets){
            Set<TicketHistory> ticketHistorySet = ticket.getTicketHistories();

            // get the latest Transaction of the ticket
            TicketTransaction latestTransaction = null;
            for (TicketHistory history : ticketHistorySet){
                TicketTransaction currentTransacion = history.getTicketTransaction();

                if(latestTransaction == null){
                    latestTransaction = history.getTicketTransaction();
                }
                if(latestTransaction.getLastModifiedAt().isBefore(currentTransacion.getLastModifiedAt())){
                    latestTransaction = currentTransacion;
                }
            }
            // add ticket to list and set the appropriate status
            TicketWrapper ticketWrapper = new TicketWrapper();
            ticketWrapper.setTicket(ticket);

            if(latestTransaction == null){
                ticketWrapper.setStatus(TicketStatus.STORNO);
            } else {
                ticketWrapper.setStatus(latestTransaction.getStatus());
            }

            ticketsWithStatus.add(ticketWrapper);
        }

        return ticketsWithStatus;
    }


}
