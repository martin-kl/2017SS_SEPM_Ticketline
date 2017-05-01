package at.ac.tuwien.inso.sepm.ticketline.server.service.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ValidationHelper {
    public static List<String> getErrorMessages(Exception exception) {
        List<String> messages = new ArrayList<>(5);
        for (Throwable t = exception.getCause(); t != null; t = t.getCause()) {
            if (t instanceof javax.validation.ConstraintViolationException) {
                ConstraintViolationException cv = (ConstraintViolationException) t;
                for(ConstraintViolation v: cv.getConstraintViolations()) {
                    messages.add(v.getMessage());
                }
                return messages;
            }
        }
        return messages;
    }
    public static String getSingleErrorMessage(Exception exception) {
        String message = "";
        for (String error: getErrorMessages(exception)) {
            message += error + ". ";
        }
        return message;
    }
}
