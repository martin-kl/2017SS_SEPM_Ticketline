package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.accounts.AccountsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomerAddEditController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomersController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.EventsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan.PerformanceDetailController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.ReservationsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.TransactionDetailController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
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
    private AccountsController accountsController;
    private CustomersController customersController;
    private ReservationsController reservationsController;
    private EventsController eventsController;
    private PerformanceDetailController performanceDetailController;

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
        initCustomersTabPane();
        initEventsTabPane();
        initAccountsTabPane();
        initReservationsTabPane();

        //add listener to reload data when tab is clicked
        tpContent.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            switch (tpContent.getSelectionModel().getSelectedItem().getId()) {
                case "news":
                    reloadNewsList();
                    break;
                case "customers":
                    reloadCustomerList();
                    break;
                case "events":
                    reloadEventList();
                    break;
                case "accounts":
                    //reloadCustomerList();
                    break;
                case "reservations":
                    reloadReservationList();
                    break;
                default:
                    log.error("invalid argument in tab pane switch, argument is = {}",
                        tpContent.getSelectionModel().getSelectedItem().getId());
                    break;
            }
        });
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

    public void showPerformanceDetailWindow(DetailedPerformanceDTO performance) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        //wrapper contains controller and loaded object
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/events/performanceDetailComponent.fxml");
        performanceDetailController = (PerformanceDetailController) wrapper
            .getController();
        dialog.setScene(new Scene((Parent) wrapper.getLoadedObject()));

        performanceDetailController.initializeData(performance);
        dialog.setTitle(BundleManager.getBundle().getString("performance.window.title"));

        dialog.setOnCloseRequest(event -> {
            performanceDetailController.handleCancel();
            performanceDetailController = null;
            event.consume();
        });
        dialog.showAndWait();
    }

    public void addEditCustomerWindow(CustomerDTO customerToEdit) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        //wrapper contains controller and loaded object
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/customers/addEditCustomer.fxml");
        CustomerAddEditController controller = (CustomerAddEditController) wrapper.getController();
        dialog.setScene(new Scene((Parent) wrapper.getLoadedObject()));

        controller.setCustomerToEdit(customerToEdit);
        if (customerToEdit != null) {
            dialog.setTitle(BundleManager.getBundle().getString("customer.edit"));
        } else {
            dialog.setTitle(BundleManager.getBundle().getString("customer.add"));
        }
        dialog = Helper.setDefaultOnCloseRequest(dialog);
        dialog.showAndWait();
    }

    public void showTransactionDetailWindow(
        DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        Stage dialog = initStage();
        //wrapper contains controller and loaded object
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetail.fxml");
        TransactionDetailController controller = (TransactionDetailController) wrapper
            .getController();
        dialog.setScene(new Scene((Parent) wrapper.getLoadedObject()));

        controller.initData(detailedTicketTransactionDTO);
        //showTransactionDetailStage(dialog);
        dialog = Helper.setDefaultOnCloseRequest(dialog);
        dialog.showAndWait();
    }

    public void showTransactionDetailWindow(List<TicketDTO> ticketDTOList,
        DetailedPerformanceDTO detailedPerformanceDTO) {
        Stage dialog = initStage();
        //wrapper contains controller and loaded object
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetail.fxml");
        TransactionDetailController controller = (TransactionDetailController) wrapper
            .getController();
        dialog.setScene(new Scene((Parent) wrapper.getLoadedObject()));

        controller.initData(ticketDTOList, detailedPerformanceDTO, performanceDetailController);
        dialog.setTitle(BundleManager.getBundle().getString("transaction.detail.title"));
        dialog.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(dialog);
            alert.setTitle(BundleManager.getBundle().getString("dialog.customer.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("dialog.customer.header"));
            alert.setContentText(BundleManager.getBundle().getString("dialog.customer.content"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && ButtonType.OK.equals(result.get())) {
                performanceDetailController.clearData(true);
                performanceDetailController = null;
            }
        });
        dialog.showAndWait();
    }

    private Stage initStage() {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        return dialog;
    }

    private void initNewsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/news/newsComponent.fxml");
        newsController = (NewsController) wrapper.getController();
        newsController.setFont(fontAwesome);
        Tab newsTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.NEWSPAPER_ALT);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        newsTab.setGraphic(newsGlyph);
        newsTab.setId("news");
        tpContent.getTabs().add(newsTab);
    }

    private void initCustomersTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/customers/customersComponent.fxml");
        customersController = (CustomersController) wrapper.getController();
        customersController.setFont(fontAwesome);
        Tab customerTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.USER);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        customerTab.setGraphic(newsGlyph);
        customerTab.setId("customers");
        tpContent.getTabs().add(customerTab);
    }

    private void initEventsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/events/eventsComponent.fxml");
        eventsController = (EventsController) wrapper.getController();
        eventsController.setFont(fontAwesome);
        Tab eventTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.CALENDAR);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        eventTab.setGraphic(newsGlyph);
        eventTab.setId("events");
        tpContent.getTabs().add(eventTab);
    }

    private void initAccountsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/accounts/accountsComponent.fxml");
        accountsController = (AccountsController) wrapper.getController();
        accountsController.setFont(fontAwesome);
        Tab accountsTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.USERS);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        accountsTab.setGraphic(newsGlyph);
        accountsTab.setId("accounts");
        tpContent.getTabs().add(accountsTab);
    }

    private void initReservationsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/reservations/reservationsComponent.fxml");
        reservationsController = (ReservationsController) wrapper.getController();
        reservationsController.setFont(fontAwesome);
        Tab reservationTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.TICKET);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        reservationTab.setGraphic(newsGlyph);
        reservationTab.setId("reservations");
        tpContent.getTabs().add(reservationTab);
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

    public void reloadCustomerList() {
        customersController.loadCustomers();
    }

    public void reloadNewsList() {
        newsController.loadNews();
    }

    public void reloadEventList() {
        eventsController.loadEvents();
    }

    private void reloadReservationList() {
        reservationsController.loadTransactions();
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

        //reload fonts (causing header reload)
        newsController.setFont(fontAwesome);
        accountsController.setFont(fontAwesome);
        customersController.setFont(fontAwesome);
        reservationsController.setFont(fontAwesome);
        eventsController.setFont(fontAwesome);

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

        //TODO implement all these methods and update them if something is changing (new button or something like this)
        newsController.reloadLanguage();
        customersController.reloadLanguage();
        eventsController.reloadLanguage();
        accountsController.reloadLanguage();
        reservationsController.reloadLanguage();

    }
}
