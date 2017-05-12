package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;

import java.awt.event.AdjustmentListener;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionListController {

    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;

    @FXML
    private TextField tfTransactionNumber;
    @FXML
    private TextField tfCustomerFirstName;
    @FXML
    private TextField tfCustomerLastName;
    @FXML
    private TextField tfPerformanceName;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnReservationDetails;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbReservationsElements;

    private FontAwesome fontAwesome;
    private static final int HEADER_ICON_SIZE = 25;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final ReservationService reservationService;

    private DetailedTicketTransactionDTO selectedTransaction;
    private VBox previousSelectedBox = null;

    private int loadedUntilPage = -1;

    private SearchState searchState = SearchState.NOTHING;


    private enum SearchState {
        NOTHING, ID, TEXT
    }

    public TransactionListController(MainController mainController, SpringFxmlLoader springFxmlLoader, ReservationService reservationService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.reservationService = reservationService;
    }

    public void reloadLanguage() {
        setTitle(BundleManager.getBundle().getString("reservation/sales.title"));

        tfTransactionNumber
            .setPromptText(
                BundleManager.getBundle().getString("reservation.prompt.transactionNumber"));
        tfCustomerFirstName
            .setPromptText(
                BundleManager.getBundle().getString("reservation.prompt.customerFirstName"));
        tfCustomerLastName
            .setPromptText(
                BundleManager.getBundle().getString("reservation.prompt.customerLastName"));
        tfPerformanceName.setPromptText(
            BundleManager.getBundle().getString("reservation.prompt.performanceName"));

        btnSearch.setText(BundleManager.getBundle().getString("reservation.search"));
        btnReservationDetails
            .setText(BundleManager.getBundle().getString("reservation.showDetails"));

        selectedTransaction = null;
        previousSelectedBox = null;
        loadTransactions();
    }

    public void setFont(FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.TICKET);
        setTitle(BundleManager.getBundle().getString("reservation/sales.title"));
    }

    private void setIcon(FontAwesome.Glyph glyph) {
        lblHeaderIcon.setGraphic(
            fontAwesome
                .create(glyph)
                .size(HEADER_ICON_SIZE));
    }

    private void setTitle(String title) {
        lblHeaderTitle.setText(title);
    }
    private boolean currentlyLoading = false;

    public void loadTransactions() {
        //delete possible entries from before
        tfTransactionNumber.setText("");
        tfCustomerFirstName.setText("");
        tfCustomerLastName.setText("");
        tfPerformanceName.setText("");
        searchState = SearchState.NOTHING;
        prepareForNewList();
        loadNext();

        scrollPane.vvalueProperty().addListener((ov, old_val, new_val) -> {
            if (vbReservationsElements.getChildren().size() == 0) return;
            if (currentlyLoading) return;
            if (new_val.floatValue() > 0.9) {
                currentlyLoading = true;
                loadNext();
            }
        });
    }


    public void handleSearch(ActionEvent actionEvent) {
        if (tfTransactionNumber.getText().length() != 0) {
            searchState = SearchState.ID;
            prepareForNewList();
            loadNext();
            return;
        }
        if (tfCustomerFirstName.getText().length() == 0
            || tfCustomerLastName.getText().trim().length() == 0
            || tfPerformanceName.getText().length() == 0) {
            ValidationException e = new ValidationException("reservation.error.emptySearch");
            e.showDialog();
            return;
            //don't change list at all
        }
        //search with customerName / performance name
        searchState = SearchState.TEXT;
        prepareForNewList();
        loadNext();
    }


    private void prepareForNewList() {
        selectedTransaction = null;
        previousSelectedBox = null;
        loadedUntilPage = -1;
        vbReservationsElements.getChildren().clear();
    }

    private void loadNext() {
        Task<List<DetailedTicketTransactionDTO>> task = new Task<List<DetailedTicketTransactionDTO>>() {
            @Override
            protected List<DetailedTicketTransactionDTO> call() throws DataAccessException {
                try {
                    switch (searchState) {
                        case NOTHING:
                            return reservationService.findTransactionsBoughtReserved(++loadedUntilPage);
                        case ID:
                            return Collections.singletonList(reservationService.findTransactionWithID(tfTransactionNumber.getText().trim()));
                        case TEXT:
                            return reservationService.findTransactionsByCustomerAndPerformance(
                                tfCustomerFirstName.getText().trim(),
                                tfCustomerLastName.getText().trim(),
                                tfPerformanceName.getText().trim(),
                                ++loadedUntilPage
                            );
                    }
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.showDialog();
                }
                return new ArrayList<>();
            }

            @Override
            protected void succeeded() {
                appendElements(getValue());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbReservationsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }


    private void appendElements(List<DetailedTicketTransactionDTO> elements) {
        Iterator<DetailedTicketTransactionDTO> iterator = elements.iterator();
        for (DetailedTicketTransactionDTO ticketTransaction : elements) {
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/transactions/transactionElement.fxml");

            ((TransactionListElementController) wrapper.getController())
                .initializeData(ticketTransaction);
            VBox reservationBox = (VBox) wrapper.getLoadedObject();

            reservationBox.setOnMouseClicked((e) -> {
                //un-mark previous selected box
                if (previousSelectedBox != null) {
                    previousSelectedBox.setStyle("-fx-background-color: inherit");
                }
                log.debug("Selected a transaction with id: " + ticketTransaction.getId());
                previousSelectedBox = reservationBox;
                reservationBox.setStyle("-fx-background-color: #2196F3");
                selectedTransaction = ticketTransaction;
            });

            vbReservationsElements.getChildren().add(reservationBox);
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                vbReservationsElements.getChildren().add(separator);
            }

        }
        currentlyLoading = false;
    }

    public void handleReservationDetails(ActionEvent actionEvent) {
        //start it with selectedTransaction as argument
        if (selectedTransaction == null) {
            log.error("Tried to see details of transaction but no transaction was selected");
            //show alert
            ValidationException e = new ValidationException("reservation.error.nothingSelected");
            e.showDialog();
            return;
        }
        log.debug("Loading Details of transaction with id: " + selectedTransaction.getId());
        mainController.showTransactionDetailWindow(selectedTransaction);
    }
}
