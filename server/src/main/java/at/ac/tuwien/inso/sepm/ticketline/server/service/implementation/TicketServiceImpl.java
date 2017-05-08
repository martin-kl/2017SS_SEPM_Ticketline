package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    private TicketTransactionRepository ticketTransactionRepository;

    public TicketServiceImpl(TicketTransactionRepository ticketTransactionRepository) {
        this.ticketTransactionRepository = ticketTransactionRepository;
    }

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
        Optional<TicketTransaction> transaction = ticketTransactionRepository.findOneById(id);
        if(transaction == null)
            throw new NotFoundException();
        if (!transaction.isPresent()) {
            throw new NotFoundException();
        }
        return transaction.get();
    }

    @Override
    public List<TicketTransaction> findTransactionsByCustomerAndLocation(String customerFirstName,
        String customerLastName, String performance) {
        //return null;
        List<TicketTransaction> result = ticketTransactionRepository
            .findByCustomerAndLocation(customerFirstName, customerLastName, performance);

        //filter all double elements
        List<TicketTransaction> filteredList = new ArrayList<>();
        for(TicketTransaction tt : result) {
            if(filteredList.size() > 0) {
                if (tt.getId() != filteredList.get(filteredList.size() - 1).getId()) {
                    filteredList.add(tt);
                }
            }else {
                filteredList.add(tt);
            }
        }
        return filteredList;
    }
}
