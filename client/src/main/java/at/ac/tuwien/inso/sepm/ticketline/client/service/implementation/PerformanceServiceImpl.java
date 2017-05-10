package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRestClient performanceRestClient;

    public PerformanceServiceImpl(PerformanceRestClient performanceRestClient) {
        this.performanceRestClient = performanceRestClient;
    }

    @Override
    public DetailedPerformanceDTO findOne(UUID id) throws ExceptionWithDialog {
        return performanceRestClient.findOne(id);
    }
}
