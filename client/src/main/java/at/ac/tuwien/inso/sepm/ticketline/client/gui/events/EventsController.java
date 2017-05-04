package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventsController {
    private static final int HEADER_ICON_SIZE = 25;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;

    private FontAwesome fontAwesome;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    /* TODO: add events service */
    /* TODO: add page specific elements, create specific fxml */
    public EventsController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add events service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    public void setFont(FontAwesome fontAwesome){
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.CALENDAR);
        setTitle(BundleManager.getBundle().getString("events.title"));
    }

    @FXML
    private void initialize() {

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


    public void loadEvents() {
        // TODO: load Events (see NewsController for example */
    }

    public void reloadLanguage() {
        setTitle(BundleManager.getBundle().getString("events.title"));
    }
}
