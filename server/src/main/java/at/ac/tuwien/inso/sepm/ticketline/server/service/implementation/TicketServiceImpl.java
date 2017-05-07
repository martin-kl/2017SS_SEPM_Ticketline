package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Override
    public List<TicketTransaction> getAllTransactions(String status) {
        TicketStatus ticketStatus;
        try {
            ticketStatus = TicketStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            log.error("Bad status for TicketStatus - status is {}", status);
            throw new BadRequestException("Bad status");
        }

        //TODO replace top100 again with all - is just so for testing everything and to load faster
        return ticketTransactionRepository.findTop100ByStatus(ticketStatus);
        //return ticketTransactionRepository.findByStatus(ticketStatus);
    }

    @Override
    public List<TicketTransaction> getAllBoughtReservedTransactions() {
        //TODO replace top100 again with all - is just so for testing everything and to load faster
        return ticketTransactionRepository
            .findTop100ByStatusOrStatusOrderByIdDesc(TicketStatus.BOUGHT, TicketStatus.RESERVED);
        //return ticketTransactionRepository.findByStatus(ticketStatus);
    }

    @Override
    public TicketTransaction findTransactionsByID(UUID id) {
        //return ticketTransactionRepository.findOne(id);
        return ticketTransactionRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<TicketTransaction> findTransactionsByCustomerAndLocation(String customer,
        String performance) {
        return null;
        //return ticketTransactionRepository.findByCustomerAndLocation(customer, performance);
    }
}
