package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SeatLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SectorLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.xml.soap.Detail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerformanceDetailController {

    private final PerformanceService performanceService;
    private final SpringFxmlLoader springFxmlLoader;
    private final MainController mainController;
    @FXML
    private Label lblPerformanceHeader;
    @FXML
    private Label lblPerformanceName;
    @FXML
    private Label lblTicketsHeader;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnContinue;
    @FXML
    private Label lblHallplanHeader;
    @FXML
    private GridPane pHallplan;
    @FXML
    private VBox vbSelectedTickets;

    private DetailedPerformanceDTO detailedPerformance;

    public PerformanceDetailController(MainController mainController, SpringFxmlLoader springFxmlLoader, PerformanceService performanceService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.performanceService = performanceService;
    }

    public void initializeData(DetailedPerformanceDTO detailedPerformance){
        log.debug("Initializing performance in PerformanceDetail: " + detailedPerformance.getName());
        this.detailedPerformance = detailedPerformance;
        reloadLanguage();
        constructHallPlan();
    }

    public void reloadLanguage(){
        lblPerformanceHeader.setText(BundleManager.getBundle().getString("performance.selected.performance.header"));
        lblPerformanceName.setText(detailedPerformance.getName());
        lblTicketsHeader.setText(BundleManager.getBundle().getString("performance.selected.tickets.header"));
        btnCancel.setText(BundleManager.getBundle().getString("performance.button.cancel"));
        btnContinue.setText(BundleManager.getBundle().getString("performance.button.continue"));

        lblHallplanHeader.setText(BundleManager.getBundle().getString("performance.hallplan.header") + " ");

        if(detailedPerformance.getLocation() instanceof SeatLocationDTO){
            lblHallplanHeader.setText(lblHallplanHeader.getText() + BundleManager.getBundle().getString("performance.type.seat"));
        } else {
            lblHallplanHeader.setText(lblHallplanHeader.getText() + BundleManager.getBundle().getString("performance.type.sector"));
        }
    }

    private void constructHallPlan(){
        if(detailedPerformance.getLocation() instanceof SectorLocationDTO) {
            // WORKING WITH SECTORS
            // building a list of sectors
            List<SectorDTO> sectorList = new ArrayList<>();
            for(TicketWrapperDTO ticketWrapper : detailedPerformance.getTicketWrapperList()){
                // each TicketWrapper contains (TicketDTO + TicketStatus)
                if(ticketWrapper.getTicket() instanceof SectorTicketDTO) {
                    sectorList.add(((SectorTicketDTO) ticketWrapper.getTicket()).getSector());
                }
            }
            log.debug("Sectors available: " + sectorList.toString());


            for(int i = 0; i < sectorList.size(); i++){
                SectorDTO sector = sectorList.get(i);
                final Label label = new Label(sector.getName());
                pHallplan.add(label, i, 0);
            }














        } else {
            // WORKING WITH SEATS
            // building a list of seats
            List<SeatDTO> seatList = new ArrayList<>();
            for(TicketWrapperDTO ticketWrapper : detailedPerformance.getTicketWrapperList()){
                // each TicketWrapper contains (TicketDTO + TicketStatus)
                if(ticketWrapper.getTicket() instanceof SectorTicketDTO) {
                    seatList.add(((SeatTicketDTO) ticketWrapper.getTicket()).getSeat());
                }
            }
            log.debug("Seats available: " + seatList.toString());
        }
    }
}

