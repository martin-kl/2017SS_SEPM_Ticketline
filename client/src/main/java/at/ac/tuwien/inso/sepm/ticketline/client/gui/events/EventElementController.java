package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EventsElementController {

    @FXML
    private Label labelEventName;

    @FXML
    private Label labelEventCategory;

    @FXML
    private VBox vbPerformanceElements;

    public void initializeData(EventDTO eventDTO) {
        labelEventName.setText(eventDTO.getName());
        labelEventName.setText("NO_CATEGORY");
        showPerformances();
    }

    private void showPerformances(){

    }

}
