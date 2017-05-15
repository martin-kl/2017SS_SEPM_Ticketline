package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomerAddEditController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomersElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerSelection {

    @FXML
    private TextField customerSearchField;
    @FXML
    private Button btnAddNewCustomer;
    @FXML
    private ScrollPane spCustomerSelection;
    @FXML
    private VBox customerSelection;
    @FXML
    private Label selectedCustomer;
    @FXML
    private Button btnContinue;
    @FXML
    private Button btnReturn;


    private CustomerDTO lastSelectedCustomer;
    private HBox previousSelectedBox = null;

    private int loadedUntilPage = -1;
    private boolean currentlySearching = false;
    private boolean currentlyLoading = false;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private final TransactionController transactionController;

    public CustomerSelection(MainController mainController, SpringFxmlLoader springFxmlLoader,
        CustomerService customerService,
        TransactionController transactionController) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
        this.transactionController = transactionController;
    }

    public void initData() {
        //drawCustomers();

        //delete possible entries from before in all search fields TODO implement the search
        currentlySearching = false;
        prepareForNewList();
        loadNext(false);

        spCustomerSelection.vvalueProperty().addListener((ov, old_val, new_val) -> {
            if (customerSelection.getChildren().size() == 0) {
                return;
            }
            if (currentlyLoading) {
                return;
            }
            if (new_val.floatValue() > 0.9) {
                currentlyLoading = true;
                loadNext(false);
            }
        });
    }

    private void prepareForNewList() {
        previousSelectedBox = null;
        lastSelectedCustomer = null;
        updateCurrentlySelectedCustomer();
        loadedUntilPage = -1;
        customerSelection.getChildren().clear();
    }


    private void loadNext(boolean deleteEverythingBeforeDrawing) {
        Task<List<CustomerDTO>> task = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws DataAccessException {
                try {
                    return customerService
                        .search(customerSearchField.getText().trim(), ++loadedUntilPage);
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.showDialog();
                    return new ArrayList<>();
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                //drawCustomers(getValue().iterator());
                if(deleteEverythingBeforeDrawing){
                    customerSelection.getChildren().clear();
                }
                appendElements(getValue());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    customerSelection.getScene().getWindow()).showAndWait();
            }

        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void appendElements(List<CustomerDTO> elements) {
        //System.out.println("actually drawing them now");
        //customerSelection.getChildren().clear();

        Iterator<CustomerDTO> iterator = elements.iterator();
        while (iterator.hasNext()) {
            CustomerDTO customer = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/customers/customersElement.fxml");

            ((CustomersElementController) wrapper.getController()).initializeData(customer);
            HBox customerBox = (HBox) wrapper.getLoadedObject();

            if (customer.equals(lastSelectedCustomer)) {
                previousSelectedBox = customerBox;
                customerBox.setStyle("-fx-background-color: #2196F3");
            }
            customerBox.setOnMouseClicked((MouseEvent e) -> {
                if (previousSelectedBox != customerBox) {
                    //set a new customer
                    if (previousSelectedBox != null) {
                        previousSelectedBox.setStyle("-fx-background-color: inherit");
                        //onSelectionChange.call(null); //deselection
                    }
                    lastSelectedCustomer = customer;
                    customerBox.setStyle("-fx-background-color: #2196F3");
                    previousSelectedBox = customerBox;
                    //onSelectionChange.call(lastSelectedCustomer);
                } else {
                    //deselection:
                    customerBox.setStyle("-fx-background-color: inherit");
                    lastSelectedCustomer = null;
                    previousSelectedBox = null;
                }
                updateCurrentlySelectedCustomer();
            });

            customerSelection.getChildren().add(customerBox);
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                customerSelection.getChildren().add(separator);
            }
        }
        currentlyLoading = false;
    }

    private void updateCurrentlySelectedCustomer() {
        if (lastSelectedCustomer != null) {
            selectedCustomer.setText(
                lastSelectedCustomer.getFirstName() + " " + lastSelectedCustomer.getLastName());
        } else {
            selectedCustomer.setText("-");
        }
    }

    public void handleContinue(ActionEvent actionEvent) {
        transactionController.onContinue(lastSelectedCustomer);
    }

    public void onSearchChange(KeyEvent keyEvent) {
        loadedUntilPage = -1;
        loadNext(true);
    }

    public void handleNewCustomer(ActionEvent actionEvent) {
        Stage stage = (Stage) transactionController.getVbTransactionDetail().getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        //wrapper contains controller and loaded object
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/customers/addEditCustomer.fxml");
        CustomerAddEditController controller = (CustomerAddEditController) wrapper.getController();
        dialog.setScene(new Scene((Parent) wrapper.getLoadedObject()));

        //tell the controller that we are coming from the customer selection
        controller.initAddCustomerFromSelection(this);
        dialog.setTitle(BundleManager.getBundle().getString("customer.add"));

        dialog = Helper.setDefaultOnCloseRequest(dialog);
        dialog.showAndWait();
    }

    public void returnFromAddCustomer(CustomerDTO customerDTO) {
        lastSelectedCustomer = customerDTO;
        loadedUntilPage = -1;
        loadNext(true);
        //drawCustomers();
        updateCurrentlySelectedCustomer();
    }

    public void handleReturnButton(ActionEvent actionEvent) {
        Stage transactionDetailStage = (Stage) transactionController.getVbTransactionDetail()
            .getScene().getWindow();
        transactionDetailStage.close();
        if (previousSelectedBox != null) {
            previousSelectedBox.setStyle("-fx-background-color: inherit");
            previousSelectedBox = null;
        }
        lastSelectedCustomer = null;
        updateCurrentlySelectedCustomer();

    }
}
