package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PaymentException extends RuntimeException {

    public PaymentException() {
        super("Something went wrong with the payment");
    }
    public PaymentException(String message) {
        super(message);
    }

}
