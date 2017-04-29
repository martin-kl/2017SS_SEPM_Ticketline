package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerAddEditController {

    @FXML
    private TextField tf_customerFirstName;
    @FXML
    private TextField tf_customerLastName;
    @FXML
    private TextField tf_customerMail;
    @FXML
    private TextField tf_customerAddress;
    @FXML
    private DatePicker dp_Birthday;

    @FXML
    private Button btn_CustomerCancel;
    @FXML
    private Button btn_CustomerOK;

    private CustomerDTO customerDTO;

    private final CustomerService customerService;
    private final MainController mainController;

    public CustomerAddEditController(CustomerService customerService,
        MainController mainController) {
        this.mainController = mainController;
        this.customerService = customerService;
    }

    public void setCustomerToEdit(CustomerDTO customerToEdit) {
        customerDTO = customerToEdit;
        tf_customerFirstName.setText(customerToEdit.getFirstName());
        tf_customerLastName.setText(customerToEdit.getLastName());
        tf_customerMail.setText(customerToEdit.getEmail());
        tf_customerAddress.setText(customerToEdit.getAddress());
        dp_Birthday.setValue(customerToEdit.getBirthday());
    }

    public void handleCustomerCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.setTitle(BundleManager.getBundle().getString("dialog.customer.title"));
        alert.setHeaderText(BundleManager.getBundle().getString("dialog.customer.header"));
        alert.setContentText(BundleManager.getBundle().getString("dialog.customer.content"));
        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent() || ButtonType.OK.equals(result.get())) {
            Stage stage = (Stage) btn_CustomerCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void handleCustomerOK(ActionEvent actionEvent) throws DataAccessException {
        String firstName = tf_customerFirstName.getText().trim();
        String lastName = tf_customerLastName.getText().trim();

        if (this.customerDTO == null) { customerDTO = new CustomerDTO(); }
        customerDTO.setFirstName(firstName);
        customerDTO.setLastName(lastName);
        customerDTO.setEmail(tf_customerMail.getText().trim());
        customerDTO.setAddress(tf_customerAddress.getText().trim());
        customerDTO.setBirthday(dp_Birthday.getValue());

        try {
            System.out.println("saving this: "+customerDTO);
            customerDTO = customerService.save(customerDTO);
            log.info(
                "controller: customer after save method has first name = {}, last name = {}, mail = {}, address = {}, birthday = {} and id = {}",
                customerDTO.getFirstName(), customerDTO.getLastName(), customerDTO.getEmail(),
                customerDTO.getAddress(), customerDTO.getBirthday(), customerDTO.getId());

            Alert alert = new Alert(AlertType.INFORMATION);
            //TODO do we have to have these error messages in two languages as well??
            alert.setHeaderText("Customer successfully saved");
            alert.setContentText("The customer with the name "+customerDTO.getFirstName()+" "+customerDTO.getLastName()+" has been successfully saved");
            alert.showAndWait();

            mainController.reloadCustomerList();
            Stage stage = (Stage) btn_CustomerCancel.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(BundleManager.getBundle().getString("default.error.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("default.error.header"));
            alert.setContentText(BundleManager.getBundle().getString("default.error.content"));
            alert.showAndWait();
        }
    }
}
