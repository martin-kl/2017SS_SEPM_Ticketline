package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationsController {

    @FXML
    private TextField tfResBillNumber;
    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfReservationID;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnReservationDetails;

    @FXML
    private VBox vbReservationsElements;
    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final ReservationService reservationService;

    public ReservationsController(MainController mainController, SpringFxmlLoader springFxmlLoader,
        ReservationService reservationService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.reservationService = reservationService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("reservation/sales.title"));
    }

    public void loadReservations() {
        ObservableList<Node> vbReservationBoxChildren = vbReservationsElements.getChildren();
        vbReservationBoxChildren.clear();
        Task<List<DetailedTicketTransactionDTO>> task = new Task<List<DetailedTicketTransactionDTO>>() {
            @Override
            protected List<DetailedTicketTransactionDTO> call() throws DataAccessException {
                try {
                    return reservationService.findReservationsWithStatus("BOUGHT");
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.showDialog();
                    return new ArrayList<>();
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                drawReservations(getValue().iterator());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbReservationsElements.getScene().getWindow()).showAndWait();
            }

            private void drawReservations(Iterator<DetailedTicketTransactionDTO> iterator) {
                while (iterator.hasNext()) {
                    DetailedTicketTransactionDTO ticketTransaction = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/reservations/reservationsElement.fxml");

                    ((ReservationsElementController) wrapper.getController()).initializeData(ticketTransaction);
                    VBox reservationBox = (VBox) wrapper.getLoadedObject();
                    /*
                    customerBox.setOnMouseClicked((e) -> {
                        handleCustomerEdit(customer);
                    });
                    */
                    vbReservationBoxChildren.add(reservationBox);
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbReservationBoxChildren.add(separator);
                    }
                }

            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    public void handleSearch(ActionEvent actionEvent) {
    }

    public void handleReservationDetails(ActionEvent actionEvent) {
    }
}
