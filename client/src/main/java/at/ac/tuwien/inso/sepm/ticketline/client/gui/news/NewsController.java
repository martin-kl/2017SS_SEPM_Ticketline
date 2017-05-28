package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class NewsController {
    private static final int HEADER_ICON_SIZE = 25;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;
    private FontAwesome fontAwesome;

    @FXML
    private VBox vbNewsElements;
    @FXML
    private Button btnAddNews;
    @FXML
    private CheckBox cbShowSeen;
    @FXML
    private ScrollPane scrollPane;


    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;

    private int loadedUntilPage = -1;
    private boolean currentlyLoading = false;
    private boolean shouldShowSeen = false;

    public void reload() {
        prepareForNewList();
        loadNext();
        scrollPane.vvalueProperty().addListener((ov, old_val, new_val) -> {
            if (vbNewsElements.getChildren().size() == 0) {
                return;
            }
            if (currentlyLoading) {
                return;
            }
            if (new_val.floatValue() > 0.9) {
                currentlyLoading = true;
                loadNext();
            }
        });
    }


    public void init() {
        reload();
        List<String> roles = authenticationInformationService.getCurrentAuthenticationTokenInfo().get().getRoles();
        if (!roles.contains("ADMIN")) {
            btnAddNews.setVisible(false);
            btnAddNews.setManaged(false); //removed from layout calculations
        } else {
            btnAddNews.setVisible(true);
            btnAddNews.setManaged(true);
        }
    }

    private boolean deleteEverythingBeforeNextRedraw = false;

    private void prepareForNewList() {
        loadedUntilPage = -1;
        deleteEverythingBeforeNextRedraw = true;
    }

    private final AuthenticationInformationService authenticationInformationService;

    public NewsController(MainController mainController, SpringFxmlLoader springFxmlLoader, NewsService newsService, AuthenticationInformationService authenticationInformationService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.authenticationInformationService = authenticationInformationService;
        this.newsService = newsService;
    }

    public void reloadLanguage() {
        setTitle(BundleManager.getBundle().getString("news.title"));
        cbShowSeen.setText(BundleManager.getBundle().getString("show.read"));
        btnAddNews.setText(BundleManager.getBundle().getString("news.add"));
    }

    public void setFont(FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        setTitle(BundleManager.getBundle().getString("news.title"));
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


    public void addNewsClicked(ActionEvent actionEvent) {
        mainController.showAddNewsWindow();
    }

    public void showSeenClicked(ActionEvent actionEvent) {
        shouldShowSeen = cbShowSeen.isSelected();
        prepareForNewList();
        loadNext();
    }

    private void loadNext() {
        Task<List<SimpleNewsDTO>> task = new Task<List<SimpleNewsDTO>>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException {
                if (shouldShowSeen) {
                    return newsService.findAll(++loadedUntilPage);
                } else {
                    return newsService.findAllUnseen(++loadedUntilPage);
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (deleteEverythingBeforeNextRedraw) {
                    vbNewsElements.getChildren().clear();
                    deleteEverythingBeforeNextRedraw = false;
                }
                appendElements(getValue());
                currentlyLoading = false;
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbNewsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void appendElements(List<SimpleNewsDTO> simpleNewsDTOList) {
        for (Iterator<SimpleNewsDTO> iterator = simpleNewsDTOList.iterator(); iterator.hasNext(); ) {
            SimpleNewsDTO news = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
            ((NewsElementController) wrapper.getController()).initializeData(news);
            Node newsElement = (Node) wrapper.getLoadedObject();
            newsElement.setOnMouseClicked((e) -> {
                mainController.showNewsDetailWindow(news);
                if (!shouldShowSeen) {
                    vbNewsElements.getChildren().remove(newsElement);
                }
            });
            vbNewsElements.getChildren().add(newsElement);
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                vbNewsElements.getChildren().add(separator);
            }
        }
    }
}
