package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public void init(DetailedNewsDTO news) {
        Blob blob = null;
        try {
            Image image = convertToJavaFXImage(blob.getBytes(1, (int) blob.length()), (int) ivImage.getFitWidth(), (int) ivImage.getFitHeight());
            ivImage.setImage(image);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lbPublishDate.setText(news.getPublishedAt().toString());
        lbTitle.setText(news.getTitle());
        lbSummaryHeader.setText(BundleManager.getBundle().getString("news.summary.header"));
        lbSummaryText.setText(news.getSummary());
        lbTextHeader.setText(BundleManager.getBundle().getString("news.text.header"));
        lbTextText.setText(news.getText());
    }

    private static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(raw);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {
            log.error("error while converting blob to javafx.scene.image, message="+ex.getLocalizedMessage());
        }
        return image;
    }

    public void handleReturnButton(ActionEvent actionEvent) {
        //TODO close window (reference to main necessary?? )
    }
}
