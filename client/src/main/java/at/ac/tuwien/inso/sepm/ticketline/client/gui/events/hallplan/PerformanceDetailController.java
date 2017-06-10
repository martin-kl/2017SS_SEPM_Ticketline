package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.HallplanService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SeatLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SectorLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.*;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class PerformanceDetailController {

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
    /* legend */
    @FXML
    private GridPane gpLegend;
    @FXML
    private Label lblLegend;
    @FXML
    private Label lblFreeSeat;
    @FXML
    private Label lblSelectedSeat;
    @FXML
    private Label lblOccupiedSeat;

    private List<Button> selectionButtonList = new ArrayList<>();

    private DetailedPerformanceDTO detailedPerformance;

    private List<TicketDTO> chosenTickets = new ArrayList<>();
    private FontAwesome fontAwesome;

    public PerformanceDetailController(MainController mainController, SpringFxmlLoader springFxmlLoader, HallplanService hallplanService, FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.hallplanService = hallplanService;
    }

    public void initializeData(DetailedPerformanceDTO detailedPerformance){
        log.debug("Initializing performance in PerformanceDetail: " + detailedPerformance.getName());
        this.detailedPerformance = detailedPerformance;
        btnContinue.setDisable(true);
        reloadLanguage();
        constructHallPlan();
    }

    private void reloadLanguage(){
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

        lblLegend.setText(BundleManager.getBundle().getString("performance.label.legend.title"));
        lblFreeSeat.setText(BundleManager.getBundle().getString("performance.label.legend.free"));
        lblSelectedSeat.setText(BundleManager.getBundle().getString("performance.label.legend.selected"));
        lblOccupiedSeat.setText(BundleManager.getBundle().getString("performance.label.legend.occupied"));
    }
    private void constructHallPlan(){
        selectionButtonList.clear();
        if(detailedPerformance.getLocation() instanceof SectorLocationDTO) {
            /* remove legend */
            setEnabledLegend(false);
            // WORKING WITH SECTORS
            // building a list of sectors
            List<SectorDTO> sectorList = hallplanService.getSectorsOfPerformance(detailedPerformance);
            for(int i = 0, column = 0, row = 0; i < sectorList.size(); i++, column = (i % 4 == 0 ? 0 : column + 1), row += (i % 4 == 0 ? 1 : 0)){
                final SectorDTO sector = sectorList.get(i);
                final SectorButton sectorButton = new SectorButton(fontAwesome, sector, detailedPerformance, chosenTickets, hallplanService);

                GridPane.setMargin(sectorButton, new Insets(5));
                sectorButton.setOnMouseClicked(e -> {
                    handleClick(sector, sectorButton);
                });
                selectionButtonList.add(sectorButton);
                pHallplan.add(sectorButton, column, row);
            }
        } else {
            /* enable legend */
            setEnabledLegend(true);
            // WORKING WITH SEATS
            // building a list of seats
            pHallplan.getChildren().clear();
            pHallplan.getRowConstraints().clear();
            pHallplan.getColumnConstraints().clear();
            pHallplan.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            pHallplan.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            List<TicketWrapperDTO> ticketList = detailedPerformance.getTicketWrapperList();
            Map<Integer, Integer> rowSizes = hallplanService.getColumCounts(detailedPerformance);
            int maxColumnSize = hallplanService.getLargestRowColumnCount(detailedPerformance);
            for (TicketWrapperDTO ticketWrapper : ticketList)
            {
                SeatTicketDTO seatTicket = (SeatTicketDTO) ticketWrapper.getTicket();
                SeatDTO seat = seatTicket.getSeat();

                SeatButton seatButton = new SeatButton(fontAwesome, ticketWrapper);
                seatButton.setOnMouseClicked(e -> {
                    handleClick(ticketWrapper, seatButton);
                });

                /* calculate a offset to align the hallplan to the center */
                int offset = ((maxColumnSize) - (rowSizes.get(seat.getRow())))/2;
                selectionButtonList.add(seatButton);
                pHallplan.add(seatButton, seat.getColumn() + offset, seat.getRow());
                GridPane.setMargin(seatButton, new Insets(1));
            }
        }
    }

    private void handleClick(SectorDTO clickedSector, SectorButton btnSector){

        SectorTicketDTO chosenSectorTicket = hallplanService.getRandomFreeSectorTicket(detailedPerformance, clickedSector, chosenTickets);
        if(chosenSectorTicket != null) {
            chosenTickets.add(chosenSectorTicket);
            btnSector.setSectorStatus(detailedPerformance, chosenTickets, hallplanService);
            loadTicketTable();
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(BundleManager.getBundle().getString("performance.dialog.no.ticket.title"));
            alert.setHeaderText(null);
            alert.setContentText(BundleManager.getBundle().getString("performance.dialog.no.ticket.body"));
            alert.showAndWait();
        }
    }

    private void handleClick(TicketWrapperDTO ticketWrapper, SeatButton seatButton){
        if(seatButton.onClick()){
            chosenTickets.add(ticketWrapper.getTicket());
        }
        else {
            if(chosenTickets.contains(ticketWrapper.getTicket())){
                chosenTickets.remove(ticketWrapper.getTicket());
            }
        }
        loadTicketTable();
    }

    private void reloadHallplan(){
        if(detailedPerformance.getLocation() instanceof SectorLocationDTO){
            for(int i = 0; i < selectionButtonList.size(); i++){
                SectorButton currButton = (SectorButton) selectionButtonList.get(i);
                currButton.setSectorStatus(detailedPerformance, chosenTickets, hallplanService);
            }
        }
        loadTicketTable();
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

            ((PerformanceTicketElementController) wrapper.getController()).initializeData(ticket);
            HBox ticketBox = (HBox) wrapper.getLoadedObject();
            ticketBox.setOnMouseClicked((e) -> {
                /* removing tickets in the list when in sector locations */
                if(detailedPerformance.getLocation() instanceof SectorLocationDTO){
                    chosenTickets.remove(ticket);
                    reloadHallplan();
                } else {
                    /* force a click to change the graphic (this basicly means unselecting a seat)*/
                    SeatButton correspondingSeat = getSeatButtonByTicket((SeatTicketDTO) ticket);
                    if(correspondingSeat != null)
                        correspondingSeat.onClick();
                    chosenTickets.remove(ticket);
                    reloadHallplan();
                }
            });
            vbTicketBoxChildren.add(ticketBox);
            if (iterator.hasNext()) {
                separator = new Separator();
                vbTicketBoxChildren.add(separator);
            }
        }
    }

    private SeatButton getSeatButtonByTicket(SeatTicketDTO seatTicketDTO){
        for(int i = 0; i < selectionButtonList.size(); i++){
            SeatButton currButton = (SeatButton) selectionButtonList.get(i);
            if(currButton.getTicketWrapper().getTicket().equals(seatTicketDTO)){
                // this button needs to update
                return currButton;
            }
        }
        return null;
    }

    public boolean handleCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(btnCancel.getScene().getWindow());

        alert.setTitle(BundleManager.getBundle().getString("dialog.customer.title"));
        alert.setHeaderText(BundleManager.getBundle().getString("dialog.customer.header"));
        alert.setContentText(BundleManager.getBundle().getString("dialog.customer.content"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && ButtonType.OK.equals(result.get())) {
            clearData(true);
            return true;
        }
        return false;
    }

    public void handleContinue(){
        //Stage stage = (Stage) btnCancel.getScene().getWindow();
        mainController.showTransactionDetailWindow(chosenTickets, detailedPerformance);
    }

    public void clearData(boolean closeStage){
        chosenTickets.clear();
        ObservableList<Node> vbTicketBoxChildren = vbSelectedTickets.getChildren();
        vbTicketBoxChildren.clear();
        if(closeStage) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    private void setEnabledLegend(boolean isEnabled){
        gpLegend.setManaged(isEnabled);
        lblLegend.setManaged(isEnabled);
        lblOccupiedSeat.setManaged(isEnabled);
        lblFreeSeat.setManaged(isEnabled);
        lblSelectedSeat.setManaged(isEnabled);

        if(isEnabled){
            // add buttons
            Button free = new Button();
            free.setMinSize(30,30);
            free.setGraphic(null);
            free.setStyle("-fx-background-color: rgba(205,255,215,0.72); "
                + "-fx-border-color: #495c50;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 2;");
            gpLegend.add(free, 0, 0);
            GridPane.setMargin(free, new Insets(2));

            Button selected = new Button();
            selected.setMinSize(30,30);
            selected.setGraphic(fontAwesome.create(FontAwesome.Glyph.USER));
            selected.setStyle("-fx-background-color: rgba(211,224,255,0.72);"
                + "-fx-border-color: #414a5c;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 2;");
            gpLegend.add(selected, 0, 1);
            GridPane.setMargin(selected, new Insets(2));

            Button occupied = new Button();
            occupied.setMinSize(30,30);
            occupied.setGraphic(fontAwesome.create(FontAwesome.Glyph.USER));
            occupied.setStyle("-fx-background-color: rgba(188,0,0,0.97);"
                + "-fx-border-color: #520000;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-background-radius: 3;"
                + "-fx-background-insets: 2;"
                + "-fx-border-width: 2;");
            gpLegend.add(occupied, 0, 2);
            GridPane.setMargin(occupied, new Insets(2));
        }
    }
}

