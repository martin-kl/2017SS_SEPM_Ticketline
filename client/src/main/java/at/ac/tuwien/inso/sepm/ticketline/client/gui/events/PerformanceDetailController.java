package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.HallplanService;
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
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.soap.Detail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerformanceDetailController {

    private final PerformanceService performanceService;
    private final SpringFxmlLoader springFxmlLoader;
    private final MainController mainController;
    private final HallplanService hallplanService;
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

    List<TicketDTO> chosenTickets = new ArrayList<>();

    public PerformanceDetailController(MainController mainController, SpringFxmlLoader springFxmlLoader, PerformanceService performanceService, HallplanService hallplanService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.performanceService = performanceService;
        this.hallplanService = hallplanService;
    }

    public void initializeData(DetailedPerformanceDTO detailedPerformance){
        log.debug("Initializing performance in PerformanceDetail: " + detailedPerformance.getName());
        this.detailedPerformance = detailedPerformance;
        btnContinue.setDisable(true);
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
            List<SectorDTO> sectorList = hallplanService.getSectorsOfPerformance(detailedPerformance);
            for(int i = 0, column = 0, row = 0; i < sectorList.size(); i++, column = (i % 4 == 0 ? 0 : column + 1), row += (i % 4 == 0 ? 1 : 0)){
                final SectorDTO sector = sectorList.get(i);
                final Button btnSector = new Button(sector.getName());
                btnSector.alignmentProperty().setValue(Pos.CENTER);
                btnSector.setTextAlignment(TextAlignment.CENTER);
                btnSector.wrapTextProperty().setValue(true);
                btnSector.setPrefSize(200,200);
                GridPane.setMargin(btnSector, new Insets(5));
                btnSector.setOnMouseClicked(e -> {
                    handleClick(sector);
                });
                pHallplan.add(btnSector, column, row);
            }
        } else {
            // WORKING WITH SEATS
            // building a list of seats
            List<SeatDTO> seatList = hallplanService.getSeatsOfPerformance(detailedPerformance);
            for(int reihe = 0, spalte = 0, i = 0; i < seatList.size(); i++, spalte++){
                if(i % 5 == 0){
                    reihe++;
                    spalte = 0;
                }
                SeatDTO seat = seatList.get(i);
                final Label label = new Label(seat.getRow() + "," + seat.getColumn());
                pHallplan.add(label, spalte, reihe);
            }

        }
    }

    private void handleClick(SectorDTO clickedSector){
        chosenTickets.add(hallplanService.getRandomFreeSectorTicket(detailedPerformance, clickedSector, chosenTickets));
        loadTicketTable();
    }

    private void handleClick(SeatDTO clickedSeat){
        // TODO: implement
    }

    private void loadTicketTable(){
        if(!chosenTickets.isEmpty())
            btnContinue.setDisable(false);
        else
            btnContinue.setDisable(true);

        ObservableList<Node> vbTicketBoxChildren = vbSelectedTickets.getChildren();
        vbTicketBoxChildren.clear();
        Separator separator = new Separator();
        vbTicketBoxChildren.add(separator);

        Iterator<TicketDTO> iterator = chosenTickets.iterator();
        while (iterator.hasNext()) {
            TicketDTO ticket = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/events/ticketElement.fxml");

            ((TicketElementController) wrapper.getController()).initializeData(ticket);
            HBox ticketBox = (HBox) wrapper.getLoadedObject();
            ticketBox.setOnMouseClicked((e) -> {
                // TODO: implement (removing or something)
            });
            vbTicketBoxChildren.add(ticketBox);
            if (iterator.hasNext()) {
                separator = new Separator();
                vbTicketBoxChildren.add(separator);
            }
        }
    }

    public boolean handleCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.setTitle(BundleManager.getBundle().getString("dialog.customer.title"));
        alert.setHeaderText(BundleManager.getBundle().getString("dialog.customer.header"));
        alert.setContentText(BundleManager.getBundle().getString("dialog.customer.content"));
        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent() || ButtonType.OK.equals(result.get())) {
            clearData();
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
            return true;
        }
        return false;
    }
    public void handleContinue(){
        // TODO: open next dialog and put the list of chosenTickets in it
    }

    public void clearData(){
        chosenTickets.clear();
        ObservableList<Node> vbTicketBoxChildren = vbSelectedTickets.getChildren();
        vbTicketBoxChildren.clear();
    }
}

