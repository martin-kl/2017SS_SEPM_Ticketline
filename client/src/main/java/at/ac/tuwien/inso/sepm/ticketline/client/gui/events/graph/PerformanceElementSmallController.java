package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.graph;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SeatLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SectorLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PerformanceElementSmallController {
    @FXML
    private Label labelPerformanceName;

    @FXML
    private Label labelStartTime;

    @FXML
    private Label labelLocationName;

    @FXML
    private Label labelType;

    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");

    public void initializeData(PerformanceDTO performanceDTO) {
        labelPerformanceName.setText(performanceDTO.getName());
        labelStartTime.setText(DTF.format(LocalDateTime.ofInstant(performanceDTO.getStartTime(), ZoneOffset.UTC)));
        labelLocationName.setText(performanceDTO.getLocation().getName());
        if(performanceDTO.getLocation() instanceof SeatLocationDTO){
            labelType.setText(BundleManager.getBundle().getString("performance.type.seat"));
        }
        else if(performanceDTO.getLocation() instanceof SectorLocationDTO){
            labelType.setText(BundleManager.getBundle().getString("performance.type.sector"));
        }
    }
}
