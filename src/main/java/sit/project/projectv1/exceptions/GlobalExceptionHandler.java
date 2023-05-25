package sit.project.projectv1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        ErrorResponse errors = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Announcement attributes validation failed", webRequest.getDescription(false).substring(4));
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.addValidationError(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotFound(ResponseStatusException exception, WebRequest webRequest) {
        ErrorResponse errors = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Announcement attributes validation failed", webRequest.getDescription(false).substring(4));
        errors.addValidationError(exception.getReason(), exception.getCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotFound(IllegalArgumentException exception, WebRequest webRequest) {
        ErrorResponse errors = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Announcement attributes validation failed", webRequest.getDescription(false).substring(4));
        errors.addValidationError("test", exception.getCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

