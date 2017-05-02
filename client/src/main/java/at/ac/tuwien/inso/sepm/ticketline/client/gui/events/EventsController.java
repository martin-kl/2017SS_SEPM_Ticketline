package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

/**
 * Created by Alex on 26.04.2017.
 */
@Slf4j
@Component
public class EventsController {
    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    /* TODO: add events service */
    /* TODO: add page specific elements, create specific fxml */
    public EventsController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add events service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.CALENDAR);
        tabHeaderController.setTitle("Events");
    }

    public void loadEvents() {
        // TODO: load Events (see NewsController for example */
    }
}
