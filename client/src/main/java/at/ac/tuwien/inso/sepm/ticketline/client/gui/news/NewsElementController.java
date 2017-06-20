package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewsElementController {

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    public void initializeData(SimpleNewsDTO simpleNewsDTO) {
        lblDate.setText(Helper.getDateAndTimeFormatter().format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
    }
}
