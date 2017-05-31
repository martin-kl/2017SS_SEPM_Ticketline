package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Callable;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Component
public class AddNewsController {

    @FXML
    private Button uploadButton;
    @FXML
    private TextField title;
    @FXML
    private TextArea summary;
    @FXML
    private TextArea text;

    private byte[] imageByteArray;
    @FXML
    private ImageView imageView;

    private final NewsService newsService;
    private final MainController mainController;

    public AddNewsController(MainController mainController, NewsService newsService) {
        this.mainController = mainController;
        this.newsService = newsService;
    }

    @FXML
    public void onSave() {
        DetailedNewsDTO news = new DetailedNewsDTO();
        news.setTitle(title.getText());
        news.setSummary(summary.getText());
        news.setText(text.getText());
        news.setImage(imageByteArray);
        save(news);
    }

    public void init() {
        imageByteArray = null;
    }

    private void save(DetailedNewsDTO news) {
        Task<DetailedNewsDTO> task = new Task<DetailedNewsDTO>() {
            @Override
            protected DetailedNewsDTO call() throws ExceptionWithDialog {
                return newsService.publish(news);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                onClose.call(null);
            }

            @Override
            protected void failed() {
                super.failed();
                ((ExceptionWithDialog) getException()).showDialog();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    @FXML
    public void onUpload() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter(
            "JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter(
            "PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilterjpg);
        fileChooser.getExtensionFilters().add(extFilterpng);
        File imageFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (imageFile != null) {
            try {
                imageByteArray = Files.readAllBytes(Paths.get(imageFile.getAbsolutePath()));
                Image image = Helper.convertToJavaFXImage(imageByteArray,
                    (int) imageView.getFitWidth(),
                    (int) imageView.getFitHeight());
                imageView.setImage(image);
            } catch (IOException e) {
                (new DataAccessException(e.getMessage(), "upload.failed", e)).showDialog();
            }
        }
    }

    Callable onClose = (o) -> {
    };

    public void setOnClose(Callable callable) {
        this.onClose = callable;
    }
}


