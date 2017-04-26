package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

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
    private CustomerAddEditController customerAddEditController;

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
        //add tabs
        initNewsTabPane();
        initCustomerTabPane();
    }@FXML
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

    private void initCustomerTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/addEditCustomer.fxml");
        customerAddEditController = (CustomerAddEditController) wrapper.getController();
        Tab customerTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph customerGlyph = fontAwesome.create(FontAwesome.Glyph.USER);
        customerGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        customerGlyph.setColor(Color.WHITE);
        customerTab.setGraphic(customerGlyph);
        tpContent.getTabs().add(customerTab);
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

    public void changeToGerman(ActionEvent actionEvent) {
        BundleManager.changeLocale(new Locale("de"));
        reloadLanguage();
    }

    public void changeToEnglish(ActionEvent actionEvent) {
        BundleManager.changeLocale(new Locale("en"));
        reloadLanguage();
    }

    private void reloadLanguage() {
        ResourceBundle bundle = BundleManager.getBundle();
        ObservableList<Menu> menuList = mbMain.getMenus();
        menuList.get(0).setText(bundle.getString("menu.application"));
        menuList.get(1).setText(bundle.getString("menu.help"));
        menuList.get(2).setText(bundle.getString("menu.language"));

        //set language for sub menu items of menu.application
        menuList.get(0).getItems().get(0).setText(bundle.getString("menu.application.exit"));

        //set language for sub menu items of menu.help
        menuList.get(1).getItems().get(0).setText(bundle.getString("menu.help.about"));

        //set language for sub menu items of menu.language
        menuList.get(2).getItems().get(0).setText(bundle.getString("menu.language.german"));
        menuList.get(2).getItems().get(1).setText(bundle.getString("menu.language.english"));

    }
}
