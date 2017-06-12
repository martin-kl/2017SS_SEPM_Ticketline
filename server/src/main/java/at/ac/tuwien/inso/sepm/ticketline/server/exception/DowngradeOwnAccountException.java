package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LENGTH_REQUIRED)
public class DowngradeOwnAccountException extends RuntimeException{

    public DowngradeOwnAccountException() {
        super("Admin cannot downgrade to seller on his own account he is logged in");
    }
    public DowngradeOwnAccountException(String message) {
        super(message);
    }

}
