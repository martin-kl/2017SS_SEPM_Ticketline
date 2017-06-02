package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;

import java.awt.*;
import java.util.*;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventsController {
    private static final int HEADER_ICON_SIZE = 25;
    private final PerformanceService performanceService;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;
    @FXML
    private VBox vbEventsElements;
    @FXML
    private Button btnManageTickets;

    /* search function buttons */
    @FXML
    private Button btnAsList;
    @FXML
    private Button btnAsGraph;
    @FXML
    private TextField tfGeneralSearch;
    @FXML
    private Button btnGeneralSearch;

    /**
     * extended search elements
     **/
    // General elements to Toggle the extended search elements
    @FXML
    private AnchorPane apExtendedFilters;
    @FXML
    private Button btnExtendedSearch;
    private boolean isExtendedSearch = false;

    // Event-Filter elements
    @FXML
    private Label lblEventFilter;
    @FXML
    private TextField tfEventSearch;
    @FXML
    private ComboBox<String> cbEventAttribute;
    @FXML
    private TextField tfArtistName;
    @FXML
    private SplitMenuButton smbArtistMatches;

    // Performance-Filter elements
    @FXML
    private Label lblPerformanceFilter;
    @FXML
    private DatePicker dpDate;
    @FXML
    private DatePicker dpStartTime;
    @FXML
    private DatePicker dpEndTime;
    @FXML
    private TextField tfPrice;
    @FXML
    private SplitMenuButton smbRoomMatches; // do we actually have this functionality?
    @FXML
    private TextField tfLocationSearch;
    @FXML
    private ComboBox<String> cbLocationAttribute;
    @FXML
    private SplitMenuButton smbLocationMatches;
    @FXML
    private ComboBox<String> cbPerformanceType;

    /**
     * swapping between graphic and list
     */
    @FXML
    private AnchorPane apListFilters;
    @FXML
    private AnchorPane apGraphFilters;

    // Graph (Top-Ten) elements
    @FXML
    private Button btnGraphSearch;
    @FXML
    private ComboBox<String> cbEventCategory;
    @FXML
    private ComboBox<String> cbMonth;
    @FXML
    private BarChart<Integer, Integer> barChartTopTen;
    @FXML
    private VBox vbPerformanceParent;



    private FontAwesome fontAwesome;
    @FXML
    private ScrollPane scrollPane;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventService eventService;
    private PerformanceDTO selectedPerformance = null;
    private VBox previousSelectedBox = null;
    private boolean currentlyLoading = false;

    private int loadedUntilPage = -1;

    private SearchState searchState = SearchState.NOTHING;


    private enum SearchState {
        NOTHING
    }


    public EventsController(MainController mainController, SpringFxmlLoader springFxmlLoader, EventService eventService, PerformanceService performanceService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.performanceService = performanceService;
    }

    public void init() {
        //this is the place to reset state
        ObservableList<Node> vbEventsBoxChildren = vbEventsElements.getChildren();
        vbEventsBoxChildren.clear();
        searchState = SearchState.NOTHING;
        prepareForNewList();
        scrollPane.vvalueProperty().addListener((ov, old_val, new_val) -> {
            if (vbEventsElements.getChildren().size() == 0) return;
            if (currentlyLoading) return;
            if (new_val.floatValue() > 0.9) {
                loadNext();
            }
        });
        loadNext();
    }
    private void initializeExtendedSearchLayout(){
        lblEventFilter.setText(BundleManager.getBundle().getString("events.filter"));
        lblPerformanceFilter.setText(BundleManager.getBundle().getString("performances.filter"));

        btnExtendedSearch.setText(BundleManager.getBundle().getString("events.extended.search"));
        btnManageTickets.setText(BundleManager.getBundle().getString("events.manage.tickets"));

        btnAsList.setGraphic(fontAwesome.create(FontAwesome.Glyph.LIST).size(25));
        btnAsGraph.setGraphic(fontAwesome.create(FontAwesome.Glyph.BAR_CHART).size(25));

        btnGeneralSearch.setText(BundleManager.getBundle().getString("search"));
        tfGeneralSearch.setPromptText(BundleManager.getBundle().getString("search") + " " + BundleManager.getBundle().getString("for") + " ..");
        tfEventSearch.setPromptText(BundleManager.getBundle().getString("search") + " " + BundleManager.getBundle().getString("for") + " ..");
        tfLocationSearch.setPromptText(BundleManager.getBundle().getString("search") + " " + BundleManager.getBundle().getString("for") + " ..");
        tfArtistName.setPromptText(BundleManager.getBundle().getString("artist.name") + " ..");
        tfPrice.setPromptText(BundleManager.getBundle().getString("performance.price") + " ..");

        dpDate.setPromptText(BundleManager.getBundle().getString("events.date") + " ..");
        dpEndTime.setPromptText(BundleManager.getBundle().getString("events.end") + " ..");
        dpStartTime.setPromptText(BundleManager.getBundle().getString("events.begin") + " ..");

        // Set the Attributes to search for
        if(cbEventAttribute.getItems().isEmpty())
            cbEventAttribute.getItems().addAll("Name", BundleManager.getBundle().getString("events.category"), BundleManager.getBundle().getString("events.description"));
        cbEventAttribute.getSelectionModel().select("Name");

        if(cbLocationAttribute.getItems().isEmpty())
            cbLocationAttribute.getItems().addAll("Name", BundleManager.getBundle().getString("location.street"),
                BundleManager.getBundle().getString("location.city"), BundleManager.getBundle().getString("location.country"), BundleManager.getBundle().getString("location.zip"));
        cbLocationAttribute.getSelectionModel().select("Name");

        if(cbPerformanceType.getItems().isEmpty())
            cbPerformanceType.getItems().addAll(BundleManager.getBundle().getString("performance.type.seat"), BundleManager.getBundle().getString("performance.type.sector"), BundleManager.getBundle().getString("events.type"));
        cbPerformanceType.getSelectionModel().select(BundleManager.getBundle().getString("events.type"));

        initializeGraphLayout();
    }
    private void initializeGraphLayout() {

        Calendar c = Calendar.getInstance();
        Map<String, Integer> months = c.getDisplayNames(Calendar.MONTH, Calendar.LONG, BundleManager.getBundle().getLocale());

        // Month combobox
        if(cbMonth.getItems().isEmpty()){
            cbMonth.getItems().add(BundleManager.getBundle().getString("performances.month"));
            cbMonth.getItems().addAll(months.keySet());
        }
        cbMonth.getSelectionModel().select(BundleManager.getBundle().getString("performances.month"));

        // Category combobox
        if(cbEventCategory.getItems().isEmpty()){
            cbEventCategory.getItems().addAll(String.valueOf(EventCategory.values()));
        }
        btnGraphSearch.setText(BundleManager.getBundle().getString("search"));
    }


        private void prepareForNewList() {
        previousSelectedBox = null;
        loadedUntilPage = -1;
        vbEventsElements.getChildren().clear();
    }

    public void reloadLanguage(boolean alreadyLoggedIn) {
        cbEventCategory.getItems().clear();
        cbMonth.getItems().clear();
        cbEventAttribute.getItems().clear();
        cbLocationAttribute.getItems().clear();
        cbPerformanceType.getItems().clear();
        smbArtistMatches.getItems().clear();
        smbLocationMatches.getItems().clear();

        initializeExtendedSearchLayout();
        setTitle(BundleManager.getBundle().getString("events.title"));
        if(alreadyLoggedIn) {
            init();
        }
    }

    public void setFont(FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.CALENDAR);
        setTitle(BundleManager.getBundle().getString("events.title"));
        // Set the extended search elements to invisible
        apExtendedFilters.setManaged(false);
        apExtendedFilters.setVisible(false);

        initializeExtendedSearchLayout();
        apGraphFilters.setManaged(false);
        apGraphFilters.setVisible(false);
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


    public void setSelectedPerformance(PerformanceDTO selectedPerformance, VBox performanceBox) {
        if (previousSelectedBox != null) {
            previousSelectedBox.setStyle("-fx-background-color: #f4f4f4");
        }
        previousSelectedBox = performanceBox;
        performanceBox.setStyle("-fx-background-color: #00afff");

        this.selectedPerformance = selectedPerformance;
    }

    public void manageTicketsHandler() {
        log.debug("Clicked manage tickets in Events Tab");

        if (selectedPerformance != null) {
            btnManageTickets.setDisable(true);
            loadDetailedPerformance(selectedPerformance);
        } else {
            ValidationException e = new ValidationException("event.error.dialog.noselection.header");
            e.showDialog();
        }

    }

    private void loadDetailedPerformance(PerformanceDTO performance) {
        Task<DetailedPerformanceDTO> task = new Task<DetailedPerformanceDTO>() {
            @Override
            protected DetailedPerformanceDTO call() throws ExceptionWithDialog {
                return performanceService.findOne(performance.getId());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                btnManageTickets.setDisable(false);
                DetailedPerformanceDTO detailedPerformance = getValue();
                log.debug("Success reading detailedPerformance: " + detailedPerformance.getName());
                mainController.showPerformanceDetailWindow(detailedPerformance);
            }

            @Override
            protected void failed() {
                super.failed();
                btnManageTickets.setDisable(false);
                ValidationException e = new ValidationException("event.error.dialog.noselection.header");
                e.showDialog();
            }
        };

        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }


    private void loadNext() {
        currentlyLoading = true;
        Task<List<EventDTO>> task = new Task<List<EventDTO>>() {
            @Override
            protected List<EventDTO> call() throws DataAccessException {
                switch (searchState) {
                    case NOTHING:
                        return eventService.findAll(++loadedUntilPage);
                }
                return new ArrayList<>();
            }

            @Override
            protected void succeeded() {
                appendElements(getValue());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbEventsElements.getScene().getWindow()).showAndWait();
            }
        };

        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void appendElements(List<EventDTO> elements) {
        for (Iterator<EventDTO> iterator = elements.iterator(); iterator.hasNext(); ) {
            EventDTO event = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/events/eventElement.fxml");
            ((EventElementController) wrapper.getController()).initializeData(event);
            vbEventsElements.getChildren().add((Node) wrapper.getLoadedObject());
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                vbEventsElements.getChildren().add(separator);
            }
        }
        currentlyLoading = false;
    }


    @FXML
    public void handleGeneralSearchClick(){
        // read all the textfields, generate query
        // TODO: implement
    }

    @FXML
    public void handleExtendedSearchClick(){
        if(apExtendedFilters.isManaged()){
            apExtendedFilters.setManaged(false);
            apExtendedFilters.setVisible(false);
        }
        else {
            apExtendedFilters.setManaged(true);
            apExtendedFilters.setVisible(true);
        }
    }
    @FXML
    public void handleGraphSearchClick(){

    }

    @FXML
    public void handleAsListClick(){
        apGraphFilters.setManaged(false);
        apGraphFilters.setVisible(false);

        apListFilters.setManaged(true);
        apListFilters.setVisible(true);
        scrollPane.setManaged(true);
        scrollPane.setVisible(true);
    }

    @FXML
    public void handleAsGraphClick(){
        apGraphFilters.setManaged(true);
        apGraphFilters.setVisible(true);

        apListFilters.setManaged(false);
        apListFilters.setVisible(false);
        scrollPane.setManaged(false);
        scrollPane.setVisible(false);
    }
}
