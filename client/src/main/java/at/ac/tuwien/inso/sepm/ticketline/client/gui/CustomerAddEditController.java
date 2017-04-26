package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerAddEditController {

    @FXML
    private TextField tf_customerName;

    private final CustomerService customerService;

//TODO add support for edit - so have a CustomerDTO object here and save, if it is a update or new customer process

    public CustomerAddEditController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void handleCustomerCancel(ActionEvent actionEvent) {
        //TODO show message that input will be lost and return
    }

    public void handleCustomerOK(ActionEvent actionEvent) throws DataAccessException {
        String inputName = tf_customerName.getText().trim();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(inputName);
        customerDTO = customerService.save(customerDTO);
        log.info("Customer succesfully saved with name = {} and id = {}", customerDTO.getName(), customerDTO.getId());
    }
}
