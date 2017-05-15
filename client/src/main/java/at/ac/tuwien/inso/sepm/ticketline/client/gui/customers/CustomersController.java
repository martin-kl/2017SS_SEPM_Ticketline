package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details.CustomerSelection;
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
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

import javax.xml.soap.Text;

@Slf4j
@Component
public class CustomersController {

    private static final int HEADER_ICON_SIZE = 25;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;
    @FXML
    private Button btnAddCustomer;

    @FXML
    private VBox main;

    @FXML
    private VBox customerSelectionParent;

    @FXML
    private TextField searchField;


    private FontAwesome fontAwesome;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private CustomerList customerList;

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

    public void initCustomers() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/customers/customerList.fxml");

        customerList = ((CustomerList) wrapper.getController());
        ScrollPane list = (ScrollPane) wrapper.getLoadedObject();
        customerSelectionParent.getChildren().clear();
        customerSelectionParent.getChildren().add(list);
        customerList.setCustomerClicked((customer, customerBox) ->{
            mainController.addEditCustomerWindow((CustomerDTO) customer);
        });
        customerList.reload("");
    }

    public void onSearchChange(KeyEvent keyEvent) {
        customerList.reload(searchField.getText());
    }


    public void handleCustomerAdd(ActionEvent actionEvent) {
        mainController.addEditCustomerWindow(null);
    }
}
