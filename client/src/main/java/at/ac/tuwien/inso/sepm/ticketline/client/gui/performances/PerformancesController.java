package at.ac.tuwien.inso.sepm.ticketline.client.gui.performances;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerformancesController {
    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    /* TODO: add performance service */
    /* TODO: add page specific elements, create specific fxml */
    public PerformancesController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add performance service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.CALENDAR_ALT);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("performances.title"));
    }

    public void loadPerformances() {
        // TODO: load Performances (see NewsController for example */
    }
}
