package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class ValidationException extends ExceptionWithDialog {
    public ValidationException() {
    }

    public ValidationException(String errorMessagePropertyName) {
        super(errorMessagePropertyName);
    }
}
