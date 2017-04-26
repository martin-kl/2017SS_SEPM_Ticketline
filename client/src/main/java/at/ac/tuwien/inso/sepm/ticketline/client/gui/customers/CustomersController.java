package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomersController {
    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    /* TODO: add customer service */
    /* TODO: add page specific elements, create specific fxml */

    public CustomersController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add customer service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        tabHeaderController.setTitle("Customers");
    }

    public void loadCustomers() {
        // TODO: load Customers (see NewsController for example */
    }

    public void handleCustomerEdit(ActionEvent actionEvent) {
        mainController.addEditCustomerWindow();
    }

    public void handleCustomerAdd(ActionEvent actionEvent) {
        mainController.addEditCustomerWindow();
    }
}
