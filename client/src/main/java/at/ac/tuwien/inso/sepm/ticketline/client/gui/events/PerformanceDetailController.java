package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerformanceDetailController {
    @FXML
    private Label lblPerformanceHeader;
    @FXML
    private Label lblPerformanceName;
    @FXML
    private Label lblTicketsHeader;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnContinue;
    @FXML
    private Pane pHallplan;
    @FXML
    private VBox vbSelectedTickets;

    private PerformanceDTO performance;

    public void initializeData(PerformanceDTO performance){
        // TODO: load complete performanceDTO
        
        reloadLanguage();

    }

    public void reloadLanguage(){
        lblPerformanceHeader.setText(BundleManager.getBundle().getString("performance.selected.performance.header"));
        lblPerformanceName.setText(performance.getName());
        lblTicketsHeader.setText(BundleManager.getBundle().getString("performance.selected.tickets.header"));
        btnCancel.setText(BundleManager.getBundle().getString("performance.button.cancel"));
        btnContinue.setText(BundleManager.getBundle().getString("performance.button.continue"));
    }
}

