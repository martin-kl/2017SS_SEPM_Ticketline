package at.ac.tuwien.inso.sepm.ticketline.client.exception;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;


public abstract class ExceptionWithDialog extends Exception {

    private String errorMessagePropertyName;


    public ExceptionWithDialog() {
        this("default.error.content");
    }

    public ExceptionWithDialog(String errorMessagePropertyName) {
        this.errorMessagePropertyName = errorMessagePropertyName;
    }

    public void showDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(BundleManager.getExceptionBundle().getString("default.error.title"));
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("default.error.header"));
            alert.setContentText(BundleManager.getExceptionBundle().getString(errorMessagePropertyName));
            alert.showAndWait();
        });
    }
}
