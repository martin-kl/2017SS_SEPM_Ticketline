package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketWrapperMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.TicketWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
public abstract class DetailedPerformanceMapperDecorator implements DetailedPerformanceMapper {

    @Autowired
    @Qualifier("delegate")
    private DetailedPerformanceMapper delegate;

    @Autowired
    private TicketWrapperMapper ticketWrapperMapper;
    @Autowired
    private PerformanceService performanceService;


    @Override
    public DetailedPerformanceDTO fromEntity(Performance performance){
        DetailedPerformanceDTO detailedPerformanceDTO = delegate.fromEntity(performance);

        List<TicketWrapper> ticketWrapperList =
            performanceService.addStatusToTickets(
                performanceService.findAllTicketsToPerformanceID(
                    detailedPerformanceDTO.getId()));

        List<TicketWrapperDTO> ticketWrapperDTOList = ticketWrapperMapper.fromEntity(ticketWrapperList);
        detailedPerformanceDTO.setTicketWrapperList(ticketWrapperDTOList);

        return detailedPerformanceDTO;
    }

}
