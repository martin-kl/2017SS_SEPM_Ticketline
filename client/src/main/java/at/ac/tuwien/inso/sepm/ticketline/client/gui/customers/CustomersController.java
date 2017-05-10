package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomersController {

    private static final int HEADER_ICON_SIZE = 25;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;
    private FontAwesome fontAwesome;

    @FXML
    private Button btnAddCustomer;
    @FXML
    private VBox vbCustomersElements;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;

    @FXML
    private void initialize() {
    }

    public CustomersController(MainController mainController, SpringFxmlLoader springFxmlLoader,
        CustomerService customerService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
    }

    public void setFont(FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.USER);
        setTitle(BundleManager.getBundle().getString("customers.title"));
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

    public void reloadLanguage() {
        setTitle(BundleManager.getBundle().getString("customers.title"));
        btnAddCustomer.setText(BundleManager.getBundle().getString("customer.add"));
    }

    public void loadCustomers() {
        ObservableList<Node> vbCustomerBoxChildren = vbCustomersElements.getChildren();
        vbCustomerBoxChildren.clear();
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
                    vbCustomersElements.getScene().getWindow()).showAndWait();
            }

            private void drawCustomers(Iterator<CustomerDTO> iterator) {
                while (iterator.hasNext()) {
                    CustomerDTO customer = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                        .loadAndWrap("/fxml/customers/customersElement.fxml");

                    ((CustomersElementController) wrapper.getController()).initializeData(customer);
                    HBox customerBox = (HBox) wrapper.getLoadedObject();
                    customerBox.setOnMouseClicked((e) -> {
                        log.debug("Selected a customer: " + customer.getFirstName() + " " + customer
                            .getLastName() + " with id = " + customer.getId());
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
        mainController.addEditCustomerWindow(customerDTO);
    }

    public void handleCustomerAdd(ActionEvent actionEvent) {
        mainController.addEditCustomerWindow(null);
    }
}
