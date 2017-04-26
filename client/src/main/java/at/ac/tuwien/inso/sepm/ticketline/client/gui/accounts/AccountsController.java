package at.ac.tuwien.inso.sepm.ticketline.client.gui.accounts;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

/**
 * Created by Alex on 26.04.2017.
 */
@Slf4j
@Component
public class AccountsController {
    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    /* TODO: add accounts service */
    /* TODO: add page specific elements, create specific fxml */
    public AccountsController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add accounts service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
        tabHeaderController.setTitle("Accounts");
    }

    public void loadAccounts() {
        // TODO: load Accounts (see NewsController for example */
    }
}
