package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SeatLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.Duration;
import java.util.*;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
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
    private ComboBox<ArtistDTO> cbArtistMatches;

    // Performance-Filter elements
    @FXML
    private Label lblPerformanceFilter;
    @FXML
    private Label lblDuration;
    @FXML
    private TextField tfDurationHours;
    @FXML
    private TextField tfDurationDays;
    @FXML
    private TextField tfDurationMinutes;
    @FXML
    private DatePicker dpStartTime;
    @FXML
    private DatePicker dpEndTime;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfLocationSearch;
    @FXML
    private ComboBox<String> cbLocationAttribute;
    @FXML
    private ComboBox<LocationDTO> cbLocationMatches;
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
    private boolean currentlyLoading = false;
    private VBox previousSelectedBox = null;

    private int loadedUntilPage = -1;

    private SearchState searchState = SearchState.NOTHING;

    // DATA
    private static SeatLocationDTO LOCATION_STANDARD = new SeatLocationDTO();
    private static ArtistDTO ARTIST_STANDARD = new ArtistDTO();
    private static String TYPE_STANDARD = "";
    private enum SearchState {
        NOTHING,
        EXTENDED,
        GRAPH
    }

    private ChangeListener<? super Number> listenerGeneral = ((ov, old_val, new_val) -> {
        if (vbEventsElements.getChildren().size() == 0) return;
        if (currentlyLoading) return;
        if (new_val.floatValue() > 0.9) {
            log.debug("called general listener...");
            loadNext(null);
        }
    });

    EventSearchDTO searchDTO;

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
        scrollPane.vvalueProperty().addListener(listenerGeneral);
        loadNext(null);

        // load all artists and locations
        handleArtistEnter();
        handleLocationEnter();
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

        dpEndTime.setPromptText(BundleManager.getBundle().getString("events.end") + " ..");
        dpStartTime.setPromptText(BundleManager.getBundle().getString("events.begin") + " ..");

        lblDuration.setText(BundleManager.getBundle().getString("duration") + ":");
        tfDurationDays.setPromptText(BundleManager.getBundle().getString("duration.days") + " ..");
        tfDurationHours.setPromptText(BundleManager.getBundle().getString("duration.hours") + " ..");
        tfDurationMinutes.setPromptText(BundleManager.getBundle().getString("duration.minutes") + " ..");
        loadComboboxes();
        initializeGraphLayout();
    }

    private void loadComboboxes(){
        LOCATION_STANDARD.setName(BundleManager.getBundle().getString("events.location") + "-Match");
        ARTIST_STANDARD.setFirstname(BundleManager.getBundle().getString("artist") + "-Match");
        TYPE_STANDARD = BundleManager.getBundle().getString("events.type");

        if(cbArtistMatches.getItems().isEmpty())
            cbArtistMatches.getItems().add(ARTIST_STANDARD);
        cbArtistMatches.getSelectionModel().select(ARTIST_STANDARD);
        cbArtistMatches.setCellFactory(
            new Callback<ListView<ArtistDTO>, ListCell<ArtistDTO>>() {
                @Override
                public ListCell<ArtistDTO> call(ListView<ArtistDTO> p) {
                    ListCell cell = new ListCell<ArtistDTO>() {
                        @Override
                        protected void updateItem(ArtistDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText("");
                            } else {
                                if(item.equals(ARTIST_STANDARD))
                                    setText(item.getFirstname());
                                else
                                    setText(item.getFirstname() + " " + item.getLastname());
                            }
                        }
                    };
                    return cell;
                }
            });
        cbArtistMatches.setButtonCell(
            new ListCell<ArtistDTO>() {
                @Override
                protected void updateItem(ArtistDTO t, boolean bln) {
                    super.updateItem(t, bln);
                    if (bln) {
                        setText("");
                    } else {
                        if(t.equals(ARTIST_STANDARD))
                            setText(t.getFirstname());
                        else
                            setText(t.getFirstname() + " " + t.getLastname());
                    }
                }
            });
        if(cbLocationMatches.getItems().isEmpty()){
            cbLocationMatches.getItems().add(LOCATION_STANDARD);
        }
        cbLocationMatches.getSelectionModel().select(LOCATION_STANDARD);
        cbLocationMatches.setCellFactory(
            new Callback<ListView<LocationDTO>, ListCell<LocationDTO>>() {
                @Override
                public ListCell<LocationDTO> call(ListView<LocationDTO> p) {
                    ListCell cell = new ListCell<LocationDTO>() {
                        @Override
                        protected void updateItem(LocationDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText("");
                            } else {
                                setText(item.getName());
                            }
                        }
                    };
                    return cell;
                }
            });
        cbLocationMatches.setButtonCell(
            new ListCell<LocationDTO>() {
                @Override
                protected void updateItem(LocationDTO t, boolean bln) {
                    super.updateItem(t, bln);
                    if (bln) {
                        setText("");
                    } else {
                        setText(t.getName());
                    }
                }
            });

        // Set the Attributes to search for
        if(cbEventAttribute.getItems().isEmpty())
            cbEventAttribute.getItems().addAll("Name", BundleManager.getBundle().getString("events.category"), BundleManager.getBundle().getString("events.description"));
        cbEventAttribute.getSelectionModel().select("Name");

        if(cbLocationAttribute.getItems().isEmpty())
            cbLocationAttribute.getItems().addAll("Name", BundleManager.getBundle().getString("location.street"),
                BundleManager.getBundle().getString("location.city"), BundleManager.getBundle().getString("location.country"), BundleManager.getBundle().getString("location.zip"));
        cbLocationAttribute.getSelectionModel().select("Name");


        if(cbPerformanceType.getItems().isEmpty())
            cbPerformanceType.getItems().addAll(BundleManager.getBundle().getString("performance.type.seat"), BundleManager.getBundle().getString("performance.type.sector"), TYPE_STANDARD);
        cbPerformanceType.getSelectionModel().select(TYPE_STANDARD);
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
        vbPerformanceParent.getChildren().clear();
        vbEventsElements.getChildren().clear();
    }

    public void reloadLanguage(boolean alreadyLoggedIn) {
        cbEventCategory.getItems().clear();
        cbMonth.getItems().clear();
        cbEventAttribute.getItems().clear();
        cbLocationAttribute.getItems().clear();
        cbPerformanceType.getItems().clear();
        cbArtistMatches.getItems().clear();
        cbLocationMatches.getItems().clear();

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
            showInvalidInputErrorDialog(BundleManager.getBundle().getString("event.error.dialog.noselection.header"));
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


    private void loadNext(EventSearchDTO searchParams) {
        if(searchParams == null)
            searchState = SearchState.NOTHING;
        else
            searchState = SearchState.EXTENDED;

        currentlyLoading = true;
        Task<List<EventDTO>> task = new Task<List<EventDTO>>() {
            @Override
            protected List<EventDTO> call() throws DataAccessException {
                try {
                    switch (searchState) {
                        case NOTHING:
                            return eventService.findAll(++loadedUntilPage);
                        case EXTENDED:
                            return eventService.search(searchParams, 0);
                    }
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.printStackTrace();
                    exceptionWithDialog.showDialog();
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

    private void loadArtists(String searchParams) {
        Task<List<ArtistDTO>> task = new Task<List<ArtistDTO>>() {
            @Override
            protected List<ArtistDTO> call() throws DataAccessException {
                try {
                    return eventService.searchArtists(searchParams, 0);
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.printStackTrace();
                    exceptionWithDialog.showDialog();
                }

                return new ArrayList<>();
            }

            @Override
            protected void succeeded() {appendArtistElements(getValue());
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

    private void loadLocations(LocationDTO searchParams) {
        Task<List<LocationDTO>> task = new Task<List<LocationDTO>>() {
            @Override
            protected List<LocationDTO> call() throws DataAccessException {
                try {
                    return eventService.searchLocations(searchParams, 0);
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.printStackTrace();
                    exceptionWithDialog.showDialog();
                }

                return new ArrayList<>();
            }

            @Override
            protected void succeeded() {appendLocationElements(getValue());
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

    private void appendArtistElements(List<ArtistDTO> elements){
        cbArtistMatches.getItems().clear();
        cbArtistMatches.getItems().add(ARTIST_STANDARD);
        cbArtistMatches.getItems().addAll(elements);
        cbArtistMatches.getSelectionModel().select(ARTIST_STANDARD);
        if(apExtendedFilters.isManaged())
            cbArtistMatches.show();
    }

    private void appendLocationElements(List<LocationDTO> elements){
        cbLocationMatches.getItems().clear();
        cbLocationMatches.getItems().add(LOCATION_STANDARD);
        cbLocationMatches.getItems().addAll(elements);
        cbLocationMatches.getSelectionModel().select(LOCATION_STANDARD);
        if(apExtendedFilters.isManaged())
            cbLocationMatches.show();
    }

    private void appendElements(List<EventDTO> elements) {
        for (Iterator<EventDTO> iterator = elements.iterator(); iterator.hasNext(); ) {
            EventDTO event = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/events/eventElement.fxml");
            ((EventElementController) wrapper.getController()).initializeData(event);
            vbEventsElements.getChildren().add((Node) wrapper.getLoadedObject());
            Separator separator = new Separator();
            vbEventsElements.getChildren().add(separator);
        }
        currentlyLoading = false;
    }


    @FXML
    public void handleGeneralSearchClick(){
        selectedPerformance = null;
        // read all the textfields, generate query
        if(apExtendedFilters.isManaged()){
            // Read all fields
            searchDTO = null;

            if(!tfEventSearch.getText().isEmpty()) {
                if (searchDTO == null)
                    searchDTO = new EventSearchDTO();
                if (cbEventAttribute.getSelectionModel().getSelectedItem().equals("Name"))
                    searchDTO.setEventName(tfEventSearch.getText());
                else if (cbEventAttribute.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("events.category")))
                    searchDTO.setEventCategory(tfEventSearch.getText());
                else if (cbEventAttribute.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("events.description")))
                    searchDTO.setDescription(tfEventSearch.getText());
            }

            // validate bigdecimal input
            if(!tfPrice.getText().isEmpty()){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();

                String priceInput = tfPrice.getText();
                DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(BundleManager.getBundle().getLocale());
                nf.setParseBigDecimal(true);

                BigDecimal bd = (BigDecimal) nf.parse(priceInput, new ParsePosition(0));

                // force a parse to see if the input is valid (this prevents inputs like '12daet' which would correspond to a BigDecimal of '12'
                try {
                    double number = Double.parseDouble(priceInput);
                } catch (NumberFormatException ex){
                    bd = null;
                }

                if(bd == null) {
                    // an error occured (input is invalid)
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(BundleManager.getExceptionBundle().getString("default.error.title"));
                    alert.setHeaderText(BundleManager.getExceptionBundle().getString("default.error.header"));
                    alert.setContentText(BundleManager.getExceptionBundle().getString("event.error.price"));
                    alert.showAndWait();

                    //clear invalid price input
                    tfPrice.setText("");
                }
                else {
                    tfPrice.setText(bd.toPlainString());
                    searchDTO.setPerformanceTicketPrice(bd);
                }
            }

            // Start and Endtime
            if(dpStartTime.getValue() != null){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();
                searchDTO.setPerformanceStartDate(dpStartTime.getValue());
            }
            if(dpEndTime.getValue() != null){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();
                searchDTO.setPerformanceEndDate(dpEndTime.getValue());
            }
            if(!cbPerformanceType.getSelectionModel().getSelectedItem().equals(TYPE_STANDARD)){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();
                if(cbPerformanceType.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("performance.type.sector"))){
                    searchDTO.setPerformanceType(PerformanceType.SECTOR);
                } else if(cbPerformanceType.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("performance.type.seat"))){
                    searchDTO.setPerformanceType(PerformanceType.SEAT);
                }
            }

            // check artist/location matches
            if(!cbArtistMatches.getSelectionModel().getSelectedItem().equals(ARTIST_STANDARD)){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();
                searchDTO.setArtistUUID(cbArtistMatches.getSelectionModel().getSelectedItem().getId());
            }
            if(!cbLocationMatches.getSelectionModel().getSelectedItem().equals(LOCATION_STANDARD)){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();
                searchDTO.setPerformanceLocationUUID(cbLocationMatches.getSelectionModel().getSelectedItem().getId());
            }

            if(!tfDurationDays.getText().isEmpty() || !tfDurationHours.getText().isEmpty() || !tfDurationMinutes.getText().isEmpty()){
                if(searchDTO == null)
                    searchDTO = new EventSearchDTO();
                Duration duration = Duration.ZERO;

                if(!tfDurationDays.getText().isEmpty()){
                    int days = 0;
                    try {
                        days = Integer.parseInt(tfDurationDays.getText());
                        if(days >= 0)
                            duration = duration.plusDays(days);
                        else
                            showInvalidInputErrorDialog(BundleManager.getExceptionBundle().getString("event.error.days"));
                    } catch (Exception e){
                        // an error occured (input is invalid)
                        showInvalidInputErrorDialog(BundleManager.getExceptionBundle().getString("event.error.days"));
                    }
                }
                if(!tfDurationHours.getText().isEmpty()){
                    int hours = 0;
                    try {
                        hours = Integer.parseInt(tfDurationHours.getText());
                        if(hours >= 0)
                            duration = duration.plusHours(hours);
                        else
                            showInvalidInputErrorDialog(BundleManager.getExceptionBundle().getString("event.error.days"));
                    } catch (Exception e){
                        // an error occured (input is invalid)
                        showInvalidInputErrorDialog(BundleManager.getExceptionBundle().getString("event.error.hours"));
                    }
                }
                if(!tfDurationMinutes.getText().isEmpty()){
                    int minutes = 0;
                    try {
                        minutes = Integer.parseInt(tfDurationMinutes.getText());
                        if(minutes >= 0)
                            duration = duration.plusMinutes(minutes);
                        else
                            showInvalidInputErrorDialog(BundleManager.getExceptionBundle().getString("event.error.days"));
                    } catch (Exception e){
                        // an error occured (input is invalid)
                        showInvalidInputErrorDialog(BundleManager.getExceptionBundle().getString("event.error.minutes"));
                    }
                }
                searchDTO.setPerformanceDuration(duration);
            }
            // start the search
            prepareForNewList();
            scrollPane.vvalueProperty().removeListener(listenerGeneral);
            loadNext(searchDTO);
        } else {
            // Read only the general text field
            if(tfGeneralSearch.getText().isEmpty()){
                prepareForNewList();
                scrollPane.vvalueProperty().addListener(listenerGeneral);
                loadNext(null);
            } else {
                searchDTO = new EventSearchDTO();
                searchDTO.setEventName(tfGeneralSearch.getText());
                prepareForNewList();
                scrollPane.vvalueProperty().removeListener(listenerGeneral);
                loadNext(searchDTO);
            }
        }
        // TODO: implement
    }

    private void showInvalidInputErrorDialog(String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(BundleManager.getExceptionBundle().getString("default.error.title"));
        alert.setHeaderText(BundleManager.getExceptionBundle().getString("default.error.header"));
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    /**
     * search for artists matching the search param
     */
    @FXML
    public void handleArtistEnter(){
        if(!tfArtistName.getText().equals("")){
            loadArtists(tfArtistName.getText());
        }
        else {
            // load all artists
            loadArtists("");
        }
    }
    /**
     * search for locations matching the search param
     */
    @FXML
    public void handleLocationEnter(){
        if(!tfLocationSearch.getText().equals("")){
            LocationDTO locationDTO = new SeatLocationDTO();
            // read input and set value
            if(cbLocationAttribute.getSelectionModel().getSelectedItem().equals("Name")){
                locationDTO.setName(tfLocationSearch.getText());
            } else if (cbLocationAttribute.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("location.street"))){
                locationDTO.setStreet(tfLocationSearch.getText());
            } else if (cbLocationAttribute.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("location.city"))){
                locationDTO.setCity(tfLocationSearch.getText());
            } else if (cbLocationAttribute.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("location.country"))){
                locationDTO.setCountry(tfLocationSearch.getText());
            } else if (cbLocationAttribute.getSelectionModel().getSelectedItem().equals(BundleManager.getBundle().getString("location.zip"))){
                locationDTO.setZipCode(tfLocationSearch.getText());
            }
            loadLocations(locationDTO);
        } else {
            // load all locations
            loadLocations(new SeatLocationDTO());
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
    public void handleAsGraphClick(){
        apGraphFilters.setManaged(true);
        apGraphFilters.setVisible(true);

        apListFilters.setManaged(false);
        apListFilters.setVisible(false);
        scrollPane.setManaged(false);
        scrollPane.setVisible(false);
    }
}
