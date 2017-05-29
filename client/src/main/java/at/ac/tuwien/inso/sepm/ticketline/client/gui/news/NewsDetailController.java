package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewsDetailController {

    @FXML
    private ScrollPane spScrollPane;
    @FXML
    private ImageView ivImage;
    @FXML
    private Label lbPublishDate;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbSummaryHeader;
    @FXML
    private Label lbSummary;
    @FXML
    private Label lbTextHeader;
    @FXML
    private Label lbText;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private NewsService newsService;

    public void init(SimpleNewsDTO simpleNewsDTO) {
        try {

            DetailedNewsDTO detailedNewsDTO = newsService.findDetailedNews(simpleNewsDTO.getId());
            if (detailedNewsDTO.getImage() != null) {
                System.out.println("1234: " + detailedNewsDTO.getImage().length);
                ivImage.setFitWidth(500);
                ivImage.setFitHeight(300);
                Image image = Helper.convertToJavaFXImage(detailedNewsDTO.getImage(),
                    (int) ivImage.getFitWidth(),
                    (int) ivImage.getFitHeight());
                ivImage.setImage(image);
            }

            lbText.setWrapText(true);
            lbSummary.setWrapText(true);
            //txSummary.wrappingWidthProperty().bind(lbSummaryHeader.getScene().getWindow().widthProperty().subtract(40));

            lbPublishDate.setText(detailedNewsDTO.getPublishedAt().format(formatter));
            lbTitle.setText(detailedNewsDTO.getTitle());
            lbSummaryHeader.setText(BundleManager.getBundle().getString("news.summary.header"));
            lbSummary.setText(detailedNewsDTO.getSummary());
            lbTextHeader.setText(BundleManager.getBundle().getString("news.text.header"));
            lbText.setText(detailedNewsDTO.getText());
        } catch (DataAccessException e) {
            log.error("error retrieving the detailed news entry for id {}, localized message: {}",
                simpleNewsDTO.getId(), e.getLocalizedMessage());

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(BundleManager.getExceptionBundle().getString("default.error.title"));
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("default.error.header"));
            alert.setContentText(BundleManager.getExceptionBundle().getString("default.error.content"));
        }
    }

    public void handleReturnButton(ActionEvent actionEvent) {
        ((Stage) ivImage.getScene().getWindow()).close();
    }
}
