package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewsDetailController {

    @FXML
    private ImageView ivImage;
    @FXML
    private Label lbPublishDate;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbSummaryHeader;
    @FXML
    private Label lbSummaryText;
    @FXML
    private Label lbTextHeader;
    @FXML
    private Label lbTextText;

    @Autowired
    private NewsService newsService;

    public void init(SimpleNewsDTO simpleNewsDTO) {
        try {
            DetailedNewsDTO detailedNewsDTO = newsService.findDetailedNews(simpleNewsDTO.getId());
            if (detailedNewsDTO.getImage() != null) {
                Image image = convertToJavaFXImage(detailedNewsDTO.getImage(),
                    (int) ivImage.getFitWidth(),
                    (int) ivImage.getFitHeight());
                ivImage.setImage(image);
            }
            lbPublishDate.setText(detailedNewsDTO.getPublishedAt().toString());
            lbTitle.setText(detailedNewsDTO.getTitle());
            lbSummaryHeader.setText(BundleManager.getBundle().getString("news.summary.header"));
            lbSummaryText.setText(detailedNewsDTO.getSummary());
            lbTextHeader.setText(BundleManager.getBundle().getString("news.text.header"));
            lbTextText.setText(detailedNewsDTO.getText());
        } catch (DataAccessException e) {
            log.error("error retrieving the detailed news entry for id {}, localized message: {}",
                simpleNewsDTO.getId(), e.getLocalizedMessage());

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(BundleManager.getExceptionBundle().getString("default.error.title"));
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("default.error.header"));
            alert.setContentText(BundleManager.getExceptionBundle().getString("default.error.content"));
        }
    }

    private static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(raw);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {
            log.error("error while converting blob to javafx.scene.image, message=" + ex
                .getLocalizedMessage());
        }
        return image;
    }

    public void handleReturnButton(ActionEvent actionEvent) {
        ((Stage) ivImage.getScene().getWindow()).close();
    }
}
