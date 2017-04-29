package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;

import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomersController {

    @FXML
    private VBox vbCustomersElements;
    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;

    public CustomersController(MainController mainController, SpringFxmlLoader springFxmlLoader,
        CustomerService customerService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        //TODO language support
        tabHeaderController.setTitle("Customers");
    }

    public void loadCustomers() {
        ObservableList<Node> vbCustomerBoxChildren = vbCustomersElements.getChildren();
        vbCustomerBoxChildren.clear();
        Task<List<CustomerDTO>> task = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws DataAccessException {
                return customerService.findAll();
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
                    vbCustomersElements.getScene().getWindow()).showAndWait();
            }

            private void drawCustomers(Iterator<CustomerDTO> iterator) {
                while (iterator.hasNext()) {
                    CustomerDTO customer = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/customers/customersElement.fxml");

                    ((CustomersElementController) wrapper.getController()).initializeData(customer);
                    HBox customerBox = (HBox) wrapper.getLoadedObject();
                    customerBox.setOnMouseClicked((e) -> {
                        handleCustomerEdit(customer);
                    });
                    vbCustomerBoxChildren.add(customerBox);
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbCustomerBoxChildren.add(separator);
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


    private void handleCustomerEdit(CustomerDTO customerDTO) {
        //TODO why does the main controller do this?
        mainController.addEditCustomerWindow(customerDTO);
    }

    public void handleCustomerAdd(ActionEvent actionEvent) {
        mainController.addEditCustomerWindow(null);
    }
}
