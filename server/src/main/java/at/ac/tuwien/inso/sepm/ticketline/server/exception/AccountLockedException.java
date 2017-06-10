package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccountLockedException  extends RuntimeException{

    public AccountLockedException() {
        super("Bad Request");
    }
    public AccountLockedException(String message) {
        super(message);
    }
}
