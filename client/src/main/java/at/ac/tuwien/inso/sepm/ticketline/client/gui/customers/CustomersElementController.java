package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;


import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
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

    public void initializeData(CustomerDTO customerDTO) {
        System.out.println("in initialize data");
        lblPrename.setText(customerDTO.getFirstName());
        lblLastname.setText(customerDTO.getLastName());
    }
}
