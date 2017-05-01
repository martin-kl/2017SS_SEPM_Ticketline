package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class ValidationException extends ExceptionWithDialog {
    public ValidationException() { super(); }

    public ValidationException(String errorMessagePropertyName) {
        super("", errorMessagePropertyName);
    }

    public ValidationException(String message, String errorMessagePropertyName) {
        super(message, errorMessagePropertyName);
    }
}
