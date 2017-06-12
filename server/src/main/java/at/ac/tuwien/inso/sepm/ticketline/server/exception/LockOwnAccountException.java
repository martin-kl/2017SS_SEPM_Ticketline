package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class LockOwnAccountException extends RuntimeException {

    public LockOwnAccountException() {
        super("Admin cannot lock his own account");
    }
    public LockOwnAccountException(String message) {
        super(message);
    }

}
