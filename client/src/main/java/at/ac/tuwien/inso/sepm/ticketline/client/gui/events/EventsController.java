package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

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

    private FontAwesome fontAwesome;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventService eventService;
    private PerformanceDTO selectedPerformance = null;
    private VBox previousSelectedBox = null;


    public EventsController(MainController mainController, SpringFxmlLoader springFxmlLoader, EventService eventService, PerformanceService performanceService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.performanceService = performanceService;
    }

    public void reloadLanguage() {
        setTitle(BundleManager.getBundle().getString("events.title"));
        loadEvents();
    }

    public void setFont(FontAwesome fontAwesome){
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.CALENDAR);
        setTitle(BundleManager.getBundle().getString("events.title"));
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


    public void setSelectedPerformance(PerformanceDTO selectedPerformance, VBox performanceBox){
        if(previousSelectedBox != null){
            previousSelectedBox.setStyle("-fx-background-color: #f4f4f4");
        }
        previousSelectedBox = performanceBox;
        performanceBox.setStyle("-fx-background-color: #2196F3");

        this.selectedPerformance = selectedPerformance;
    }

    public void manageTicketsHandler(){
        log.debug("Clicked manage tickets in Events Tab");

        if(selectedPerformance!=null){
            btnManageTickets.setDisable(true);
            loadDetailedPerformance(selectedPerformance);
        }
        else
        {
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

    public void loadEvents() {
        ObservableList<Node> vbEventsBoxChildren = vbEventsElements.getChildren();
        vbEventsBoxChildren.clear();

        Task<List<EventDTO>> task = new Task<List<EventDTO>>() {
            @Override
            protected List<EventDTO> call() throws DataAccessException {
                return eventService.findAll();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                for (Iterator<EventDTO> iterator = getValue().iterator(); iterator.hasNext(); ) {
                    EventDTO event = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/events/eventElement.fxml");
                    ((EventElementController) wrapper.getController()).initializeData(event);
                    vbEventsBoxChildren.add((Node) wrapper.getLoadedObject());
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbEventsBoxChildren.add(separator);
                    }
                }
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



}
