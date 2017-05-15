package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomerAddEditController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomerList;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    private Label selectedCustomer;
    @FXML
    private Button btnContinue;
    @FXML
    private Button btnReturn;
    @FXML
    private VBox customerSelectionParent;


    private CustomerDTO lastSelectedCustomer;
    private HBox previousSelectedBox = null;


    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private final TransactionController transactionController;

    private CustomerList customerList;

    public CustomerSelection(MainController mainController, SpringFxmlLoader springFxmlLoader,
        CustomerService customerService,
        TransactionController transactionController) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
        this.transactionController = transactionController;

    }

    public void initData() {
        previousSelectedBox = null;
        lastSelectedCustomer = null;
        redrawCurrentlySelectedCustomer();
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/customers/customerList.fxml");
        customerList = ((CustomerList) wrapper.getController());
        ScrollPane list = (ScrollPane) wrapper.getLoadedObject();
        customerSelectionParent.getChildren().clear();
        customerSelectionParent.getChildren().add(list);
        customerList.reload("");


        customerList.setWillDrawCustomer((c, cB) -> {
            CustomerDTO customer = (CustomerDTO) c; HBox customerBox = (HBox) cB;
            if (customer.equals(lastSelectedCustomer)) {
                previousSelectedBox = customerBox;
                customerBox.setStyle("-fx-background-color: #2196F3");
            }
        });

        customerList.setCustomerClicked((c, cB) -> {
            CustomerDTO customer = (CustomerDTO) c; HBox customerBox = (HBox) cB;
            if (previousSelectedBox != customerBox) {
                //set a new customer
                if (previousSelectedBox != null) {
                    previousSelectedBox.setStyle("-fx-background-color: inherit");
                }
                lastSelectedCustomer = customer;
                customerBox.setStyle("-fx-background-color: #2196F3");
                previousSelectedBox = customerBox;
            } else {
                //deselection:
                customerBox.setStyle("-fx-background-color: inherit");
                lastSelectedCustomer = null;
                previousSelectedBox = null;
            }
            redrawCurrentlySelectedCustomer();
        });
    }

    private void redrawCurrentlySelectedCustomer() {
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
        customerList.reload(customerSearchField.getText());
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
        customerList.reload(customerSearchField.getText());
        redrawCurrentlySelectedCustomer();
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
        redrawCurrentlySelectedCustomer();

    }
}
