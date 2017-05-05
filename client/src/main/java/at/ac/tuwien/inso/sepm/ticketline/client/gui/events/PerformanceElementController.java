package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PerformanceElementController {
    @FXML
    private Label labelPerformanceName;

    @FXML
    private Label labelStartTime;

    @FXML
    private Label labelEndTime;

    @FXML
    private Label labelLocationLabelling;

    @FXML
    private Label labelLocationName;

    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

    public void initializeData(PerformanceDTO performanceDTO) {
        labelPerformanceName.setText(performanceDTO.getName());
        labelStartTime.setText(DTF.format(LocalDateTime.ofInstant(performanceDTO.getStartTime(), ZoneOffset.UTC)));
        labelEndTime.setText(DTF.format(LocalDateTime.ofInstant(performanceDTO.getEndTime(), ZoneOffset.UTC)));
        labelLocationName.setText(performanceDTO.getLocation().getName());
        labelLocationLabelling.setText(BundleManager.getBundle().getString("events.location") + ":");
    }
}
