package sit.project.projectv1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResponseStatusValidationException extends ResponseStatusException {
    private String fieldName;

    public ResponseStatusValidationException(HttpStatus httpStatus, String fieldName, String message) {
        super(httpStatus, message);
        this.fieldName = fieldName;
    }

    public String getFieldName () {
        return  fieldName;
    }
}
