package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    private static final int TAB_ICON_FONT_SIZE = 20;

    @FXML
    private StackPane spMainContent;

    @FXML
    private ProgressBar pbLoadingProgress;

    @FXML
    private TabPane tpContent;

    @FXML
    private MenuBar mbMain;

    private Node login;

    private final SpringFxmlLoader springFxmlLoader;
    private final FontAwesome fontAwesome;
    private NewsController newsController;

    public MainController(
        SpringFxmlLoader springFxmlLoader,
        FontAwesome fontAwesome,
        AuthenticationInformationService authenticationInformationService
    ) {
        this.springFxmlLoader = springFxmlLoader;
        this.fontAwesome = fontAwesome;
        authenticationInformationService.addAuthenticationChangeListener(
            authenticationTokenInfo -> setAuthenticated(null != authenticationTokenInfo));
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> mbMain.setUseSystemMenuBar(true));
        pbLoadingProgress.setProgress(0);
        login = (Node) springFxmlLoader.load("/fxml/authenticationComponent.fxml");
        spMainContent.getChildren().add(login);
        initNewsTabPane();
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    private void aboutApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene((Parent) springFxmlLoader.load("/fxml/aboutDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("dialog.about.title"));
        dialog.showAndWait();
    }

    private void initNewsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/news/newsComponent.fxml");
        newsController = (NewsController) wrapper.getController();
        Tab newsTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.NEWSPAPER_ALT);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        newsTab.setGraphic(newsGlyph);
        tpContent.getTabs().add(newsTab);
    }

    private void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            if (spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().remove(login);
            }
            newsController.loadNews();
        } else {
            if (!spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().add(login);
            }
        }
    }

    public void setProgressbarProgress(double progress) {
        pbLoadingProgress.setProgress(progress);
    }
}
