package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.*;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class CustomerList {
    @FXML
    private VBox customerList;

    @FXML
    private ScrollPane scrollPane;


    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private final MainController mainController;

    private String search = "";
    private int loadedUntilPage = -1;
    private boolean currentlyLoading = false;



    public CustomerList(MainController mainController, SpringFxmlLoader springFxmlLoader,
                             CustomerService customerService) {
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
        this.mainController = mainController;
    }

    public void reload(String search) {
        this.search = search;
        prepareForNewList();
        loadNext();
        scrollPane.vvalueProperty().addListener((ov, old_val, new_val) -> {
            if (customerList.getChildren().size() == 0) {
                return;
            }
            if (currentlyLoading) {
                return;
            }
            if (new_val.floatValue() > 0.9) {
                currentlyLoading = true;
                loadNext();
            }
        });
    }

    private boolean deleteEverythingBeforeNextRedraw = false;
    private void prepareForNewList() {
        loadedUntilPage = -1;
        deleteEverythingBeforeNextRedraw = true;
    }


    private void loadNext() {
        Task<List<CustomerDTO>> task = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws DataAccessException {
                try {
                    return customerService
                        .search(search, ++loadedUntilPage);
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.showDialog();
                    return new ArrayList<>();
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if(deleteEverythingBeforeNextRedraw){
                    customerList.getChildren().clear();
                    deleteEverythingBeforeNextRedraw = false;
                }
                appendElements(getValue());
                currentlyLoading = false;
            }

            @Override
            protected void failed() {
                super.failed();
                currentlyLoading = false;
                JavaFXUtils.createExceptionDialog(getException(),
                    customerList.getScene().getWindow()).showAndWait();
            }

        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private TwoArgCallable willDrawCustomer = (a1, a2) -> {};
    private TwoArgCallable customerClicked = (a1, a2) -> {};

    public void setWillDrawCustomer(TwoArgCallable willDrawCustomer) {
        this.willDrawCustomer = willDrawCustomer;
    }

    public void setCustomerClicked(TwoArgCallable customerClicked) {
        this.customerClicked = customerClicked;
    }

    private void appendElements(List<CustomerDTO> elements) {
        Iterator<CustomerDTO> iterator = elements.iterator();
        while (iterator.hasNext()) {
            CustomerDTO customer = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/customers/customersElement.fxml");

            ((CustomersElementController) wrapper.getController()).initializeData(customer);
            HBox customerBox = (HBox) wrapper.getLoadedObject();

            willDrawCustomer.call(customer, customerBox);


            customerBox.setOnMouseClicked((e) -> {
                customerClicked.call(customer, customerBox);

            });
            customerList.getChildren().add(customerBox);
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                customerList.getChildren().add(separator);
            }
        }
    }

}
