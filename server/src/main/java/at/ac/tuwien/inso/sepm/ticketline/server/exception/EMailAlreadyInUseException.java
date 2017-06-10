package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class EMailAlreadyInUseException extends RuntimeException{

    public EMailAlreadyInUseException() {
        super("EMail is already in use");
    }
    public EMailAlreadyInUseException(String message) {
        super(message);
    }

}
