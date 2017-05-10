package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationController {

    @FXML
    private Label lbUsername;
    @FXML
    private TextField txtUsername;
    @FXML
    private Label lbPassword;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnAuthenticate;

    private final AuthenticationService authenticationService;

    private final MainController mainController;

    public AuthenticationController(AuthenticationService authenticationService, MainController mainController) {
        this.authenticationService = authenticationService;
        this.mainController = mainController;
    }

    public void reloadLanguage() {
        lbUsername.setText(BundleManager.getBundle().getString("authenticate.username"));
        lbPassword.setText(BundleManager.getBundle().getString("authenticate.password"));
        btnAuthenticate.setText(BundleManager.getBundle().getString("authenticate.authenticate"));
        txtUsername.setPromptText(BundleManager.getBundle().getString("authenticate.username"));
        txtPassword.setPromptText(BundleManager.getBundle().getString("authenticate.password"));
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        Task<AuthenticationTokenInfo> task = new Task<AuthenticationTokenInfo>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtUsername.getText())
                        .password(txtPassword.getText())
                        .build());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    ((Node) actionEvent.getTarget()).getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

}