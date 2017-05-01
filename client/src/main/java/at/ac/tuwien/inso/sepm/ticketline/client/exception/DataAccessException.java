package at.ac.tuwien.inso.sepm.ticketline.client.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataAccessException extends ExceptionWithDialog {
    public DataAccessException() { super(); }
    public DataAccessException(String message) {
        super();
        log.debug(message);
    }
    public DataAccessException(String message, Throwable cause) {
        super();
        log.debug(message);
    }

}
