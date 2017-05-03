package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

/**
 * Created by Alex on 26.04.2017.
 */
@Slf4j
@Component
public class ReservationsController {
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

    /* TODO: add reservations service */
    /* TODO: add page specific elements, create specific fxml */
    public ReservationsController(MainController mainController, SpringFxmlLoader springFxmlLoader /* TODO: add reservations service */) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    public void setFont(FontAwesome fontAwesome){
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.TICKET);
        setTitle(BundleManager.getBundle().getString("reservation/sales.title"));
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

    public void loadReservations() {
        // TODO: load Reservations (see NewsController for example */
    }
}
