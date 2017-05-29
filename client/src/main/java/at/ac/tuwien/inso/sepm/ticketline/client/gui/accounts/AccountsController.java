package at.ac.tuwien.inso.sepm.ticketline.client.gui.accounts;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountsController {
    private static final int HEADER_ICON_SIZE = 25;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;
    private FontAwesome fontAwesome;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    @FXML
    private void initialize() {}

    /* TODO: add accounts service */
    /* TODO: add page specific elements, create specific fxml */
    public AccountsController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add accounts service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }


    public void setFont(FontAwesome fontAwesome){
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.USERS);
        setTitle(BundleManager.getBundle().getString("accounts.title"));
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

    public void loadAccounts() {
        // TODO: load Accounts (see NewsController for example */
    }

    public void reloadLanguage(boolean alreadyLoggedIn) {
        setTitle(BundleManager.getBundle().getString("accounts.title"));
    }
}
