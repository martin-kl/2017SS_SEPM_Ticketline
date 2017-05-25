package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Callable;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Debouncer;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
    private CustomerList customerList;

    public CustomersController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
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

    public void reloadLanguage(boolean alreadyLoggedIn) {
        setTitle(BundleManager.getBundle().getString("customers.title"));
        searchField.setPromptText(BundleManager.getBundle().getString("search"));
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
        customerList.reload(searchField.getText().trim());
    }

    Debouncer<Integer> d = new Debouncer<>(o -> customerList.reload(searchField.getText()), 250);
    public void onSearchChange(KeyEvent keyEvent) {
        d.call(1);
    }

    public void handleCustomerAdd(ActionEvent actionEvent) {
        mainController.addEditCustomerWindow(null);
    }
}
