package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EventElementController {

    @FXML
    private Label labelEventName;

    @FXML
    private Label labelEventCategoryLabelling;

    @FXML
    private Label labelEventCategory;

    @FXML
    private Label labelDescription;

    @FXML
    private VBox vbPerformanceElements;

    @FXML
    private Label labelPerformancenameLabelling;

    @FXML
    private Label labelBegintimeLabelling;

    @FXML
    private Label labelEndtimeLabelling;

    @FXML
    private Label labelLocationLabelling;

    @FXML
    private Label labelTypeLabelling;


    private EventsController eventsController;

    private final SpringFxmlLoader springFxmlLoader;

    public EventElementController(SpringFxmlLoader springFxmlLoader, EventsController eventsController){
        this.eventsController = eventsController;
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initializeData(EventDTO eventDTO) {
        labelEventName.setText(eventDTO.getName());
        labelEventCategoryLabelling.setText(BundleManager.getBundle().getString("events.category"));
        labelEventCategory.setText(eventDTO.getCategory().toString());
        labelDescription.setText(eventDTO.getDescription());
        showPerformances(eventDTO);
        reloadLanguage();
    }

    private void reloadLanguage(){
        labelEventCategoryLabelling.setText(BundleManager.getBundle().getString("events.category"));

        labelPerformancenameLabelling.setText(BundleManager.getBundle().getString("events.performance.name"));
        labelBegintimeLabelling.setText(BundleManager.getBundle().getString("events.begin"));
        labelEndtimeLabelling.setText(BundleManager.getBundle().getString("events.end"));
        labelLocationLabelling.setText(BundleManager.getBundle().getString("events.location"));
        labelTypeLabelling.setText(BundleManager.getBundle().getString("events.type"));
    }

    private void showPerformances(EventDTO eventDTO){
        ObservableList<Node> vbPerformanceChildren = vbPerformanceElements.getChildren();
        vbPerformanceChildren.clear();

        for(PerformanceDTO performanceDTO : eventDTO.getPerformances()){
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/events/performanceElement.fxml");
            ((PerformanceElementController) wrapper.getController()).initializeData(performanceDTO);

            VBox performanceBox = (VBox) wrapper.getLoadedObject();
            performanceBox.setOnMouseClicked((e) -> {
                handlePerformanceClick(performanceDTO, performanceBox);
            });
            vbPerformanceChildren.add((Node) wrapper.getLoadedObject());
        }
    }

    private void handlePerformanceClick(PerformanceDTO performanceDTO, VBox performanceBox){
        log.debug("Selected a performance: " + performanceDTO.getName());
        eventsController.setSelectedPerformance(performanceDTO, performanceBox);
    }
}
