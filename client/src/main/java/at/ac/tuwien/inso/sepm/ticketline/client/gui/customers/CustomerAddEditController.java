package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.CustomerSelection;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerAddEditController {

    @FXML
    private Label lb_CustomerHeadline;
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
    /**
     * if we are coming from the customer selection after the "Saalplan" then this flag is set to
     * true - because we have to handle it differently after the creation
     */
    private boolean fromSelection;
    private CustomerSelection customerSelection;

    public CustomerAddEditController(CustomerService customerService,
        MainController mainController) {
        this.mainController = mainController;
        this.customerService = customerService;
    }

    public void setCustomerToEdit(CustomerDTO customerToEdit) {
        fromSelection = false;
        customerSelection = null;

        customerDTO = customerToEdit;
        if (customerToEdit == null) return;

        lb_CustomerHeadline.setText(BundleManager.getBundle().getString("customer.edit"));
        tf_customerFirstName.setText(customerToEdit.getFirstName());
        tf_customerLastName.setText(customerToEdit.getLastName());
        tf_customerMail.setText(customerToEdit.getEmail());
        tf_customerAddress.setText(customerToEdit.getAddress());
        dp_Birthday.setValue(customerToEdit.getBirthday());
    }

    public void initAddCustomerFromSelection(CustomerSelection customerSelection) {
        fromSelection = true;
        customerDTO = null;
        this.customerSelection = customerSelection;
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

    public void handleCustomerOK(ActionEvent actionEvent) {
        String firstName = tf_customerFirstName.getText().trim();
        String lastName = tf_customerLastName.getText().trim();

        if (this.customerDTO == null) { customerDTO = new CustomerDTO(); }
        customerDTO.setFirstName(firstName);
        customerDTO.setLastName(lastName);
        customerDTO.setEmail(tf_customerMail.getText().trim());
        customerDTO.setAddress(tf_customerAddress.getText().trim());
        customerDTO.setBirthday(dp_Birthday.getValue());

        try {
            customerDTO = customerService.save(customerDTO);
            log.info(
                "controller: customer after save method has first name = {}, last name = {}, mail = {}, address = {}, birthday = {} and id = {}",
                customerDTO.getFirstName(), customerDTO.getLastName(), customerDTO.getEmail(),
                customerDTO.getAddress(), customerDTO.getBirthday(), customerDTO.getId());

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(BundleManager.getBundle().getString("customer.saved.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("customer.saved.header"));
            alert.showAndWait();

            if(fromSelection) {
                //TODO return to selection
                customerSelection.returnFromAddCustomer(customerDTO);
            }else {
                mainController.reloadCustomerList();
            }
            //close this stage
            Stage stage = (Stage) btn_CustomerCancel.getScene().getWindow();
            stage.close();
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }
    }
}
