package at.ac.tuwien.inso.sepm.ticketline.client.gui.principals;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PrincipalService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.TwoArgCallable;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PrincipalRole;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Component
public class PrincipalAddEditController {

    PrincipalDTO principalDTO;
    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private ComboBox<String> type;

    @FXML
    private Button lockButton;

    @FXML
    private Button resetButton;

    PrincipalService principalService;

    public PrincipalAddEditController(PrincipalService principalService) {
        this.principalService = principalService;
    }

    public void setToEdit(PrincipalDTO principalDTO) {
        type.setItems(FXCollections.observableArrayList(
            BundleManager.getBundle().getString("principal.admin"),
            BundleManager.getBundle().getString("principal.seller")
        ));

        if (principalDTO == null) {
            lockButton.setVisible(false);
            resetButton.setVisible(false);
            type.setValue(BundleManager.getBundle().getString("principal.seller"));
            return;
        }
        this.principalDTO = principalDTO;

        username.setText(principalDTO.getUsername());
        password.setText("");
        confirmPassword.setText("");
        email.setText(principalDTO.getEmail());
        type.setValue(principalDTO.getPrincipalRole() == PrincipalRole.ADMIN ? BundleManager.getBundle().getString("principal.admin") : BundleManager.getBundle().getString("principal.seller"));
        setLockButton();
    }

    public void handleCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.setTitle(BundleManager.getBundle().getString("dialog.customer.title"));
        alert.setHeaderText(BundleManager.getBundle().getString("dialog.customer.header"));
        alert.setContentText(BundleManager.getBundle().getString("dialog.customer.content"));
        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent() || ButtonType.OK.equals(result.get())) {
            Stage stage = (Stage) email.getScene().getWindow();
            stage.close();
        }
    }

    private void setLockButton() {
        lockButton.setText(BundleManager.getBundle().getString(principalDTO.getLocked() ? "principal.unlock" : "principal.lock"));
    }

    private boolean passwordEntriesOK() {
        if (password.getText().equals("") && confirmPassword.getText().equals("")) {
            return true;
        }
        if (!password.getText().equals(confirmPassword.getText())) {
            (new ValidationException("principal.passwords_not_equal")).showDialog();
            return false;
        }
        return true;
    }

    TwoArgCallable onDone = (a, b) -> {};
    public void setOnDone(TwoArgCallable a) {
        this.onDone = a;
    }

    public void handleOK(ActionEvent actionEvent) {
        if (!passwordEntriesOK()) return;
        if (this.principalDTO == null) { principalDTO = new PrincipalDTO(); principalDTO.setLocked(false); }
        principalDTO.setUsername(username.getText());
        principalDTO.setEmail(email.getText());
        principalDTO.setNewPassword(password.getText());
        principalDTO.setPrincipalRole(
            type.getValue().equals(BundleManager.getBundle().getString("principal.admin")) ? PrincipalRole.ADMIN : PrincipalRole.SELLER
        );


        try {
            principalDTO = principalService.save(principalDTO);

            this.onDone.call(null, null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(BundleManager.getBundle().getString("principal.saved.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("principal.saved.header"));
            alert.showAndWait();


            //close this stage
            Stage stage = (Stage) email.getScene().getWindow();
            stage.close();
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }
    }

    private String generateNewPassword() {
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&*()-_=+[{]}\\|:\'\"<.>")
            .toCharArray();
        return RandomStringUtils
            .random(10, 0, possibleCharacters.length - 1, false, false,
                possibleCharacters, new SecureRandom());
    }

    public void resetClicked(ActionEvent e) {
        if (principalDTO == null) return;

        String newPassword = generateNewPassword();
        principalDTO.setNewPassword(newPassword);
        try {
            principalService.save(principalDTO);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(BundleManager.getExceptionBundle().getString("principal.reset.title"));
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("principal.reset.header"));
            alert.setContentText(BundleManager.getExceptionBundle().getString("principal.reset.new.password.is") +  newPassword);
            alert.showAndWait();
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }
    }

    public void lockClicked(ActionEvent e) {
        if (principalDTO == null) return;
        try {
            principalService.setLocked(principalDTO.getId(), !principalDTO.getLocked());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            boolean locked = !principalDTO.getLocked();
            if (locked) {
                alert.setTitle(BundleManager.getExceptionBundle().getString("principal.was.locked"));
                alert.setHeaderText(BundleManager.getExceptionBundle().getString("principal.was.locked.header"));
                alert.setContentText(BundleManager.getExceptionBundle().getString("principal.was.locked.content"));
            } else {
                alert.setTitle(BundleManager.getExceptionBundle().getString("principal.was.unlocked"));
                alert.setHeaderText(BundleManager.getExceptionBundle().getString("principal.was.unlocked.header"));
                alert.setContentText(BundleManager.getExceptionBundle().getString("principal.was.unlocked.content"));
            }
            principalDTO.setLocked(!principalDTO.getLocked());
            setLockButton();
            onDone.call(null, null);
            alert.showAndWait();
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }
    }
}


