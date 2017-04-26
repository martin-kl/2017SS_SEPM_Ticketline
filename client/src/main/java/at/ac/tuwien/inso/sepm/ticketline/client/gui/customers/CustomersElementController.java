package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomersElementController {

    @FXML
    private Label lblPrename;

    @FXML
    private Label lblLastname;

    // TODO: Implement SimpleCustomerDTO
    /*public void initializeData(SimpleCustomerDTO simpleCustomerDTO) {
        lblPrename.setText(simpleCustomerDTO.getPrename());
        lblLastname.setText(simpleCustomerDTO.getLastName());
    }*/

}
