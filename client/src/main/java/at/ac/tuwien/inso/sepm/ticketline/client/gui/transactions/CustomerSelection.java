package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomersElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.el.LambdaExpression;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerSelection {

    @FXML
    private TextField customerSearchField;

    @FXML
    private Button btnAddNewCustomer;

    @FXML
    private VBox customerSelection;

    @FXML
    private Label selectedCustomer;

    @FXML
    private Button btnContinue;


    private LambdaExpression onSelectionChange;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;

    public CustomerSelection(MainController mainController, SpringFxmlLoader springFxmlLoader, CustomerService customerService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
    }


    public void setOnSelectionChange(LambdaExpression onSelectionChange) {
        this.onSelectionChange = onSelectionChange;
    }

    public void reloadCustomers() {
        drawCustomers();
    }



    private void drawCustomers() {
        Task<List<CustomerDTO>> task = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws DataAccessException {
                try {
                    return customerService.findAll();
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.showDialog();
                    return new ArrayList<>();
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                drawCustomers(getValue().iterator());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    customerSelection.getScene().getWindow()).showAndWait();
            }

            private void drawCustomers(Iterator<CustomerDTO> iterator) {
                customerSelection.getChildren().removeAll();
                while (iterator.hasNext()) {
                    CustomerDTO customer = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/customers/customersElement.fxml");

                    ((CustomersElementController) wrapper.getController()).initializeData(customer);
                    HBox customerBox = (HBox) wrapper.getLoadedObject();
                    customerBox.setOnMouseClicked((e) -> {
                        onSelectionChange.invoke(customer);
                    });
                    customerSelection.getChildren().add(customerBox);
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        customerSelection.getChildren().add(separator);
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
}
