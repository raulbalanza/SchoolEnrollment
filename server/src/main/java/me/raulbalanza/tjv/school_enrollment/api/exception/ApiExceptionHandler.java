package me.raulbalanza.tjv.school_enrollment.api.exception;

import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle unknown entities
    @ExceptionHandler(UnknownEntityException.class)
    public ResponseEntity<?> handleNoDataFoundException(UnknownEntityException ex, WebRequest request) {

        return new ResponseEntity<>(Map.of(
                "error", "Unknown entity",
                "message", ex.getMessage()
        ), HttpStatus.NOT_FOUND);

    }

    // Handle conflicting entities
    @ExceptionHandler(EntityStateException.class)
    public ResponseEntity<?> handleNoDataFoundException(EntityStateException ex, WebRequest request) {

        return new ResponseEntity<>(Map.of(
                "error", "Conflicting entity",
                "message", ex.getMessage()
        ), HttpStatus.CONFLICT);

    }

    // Handle conflicting entities
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameters(InvalidParameterException ex, WebRequest request) {

        return new ResponseEntity<>(Map.of(
                "error", "Invalid values for the provided parameters",
                "message", ex.getMessage()
        ), HttpStatus.BAD_REQUEST);

    }

    // Handle missing attributes
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        String wrongAttributes = "";

        for (ConstraintViolation cv : ex.getConstraintViolations()){
            if (!wrongAttributes.isEmpty()) wrongAttributes += ", ";
            wrongAttributes += cv.getMessage();
        }

        return new ResponseEntity<>(Map.of(
                "error", "Missing or invalid values for one or more attributes",
                "message", "Incorrect attributes: " + wrongAttributes
        ), HttpStatus.BAD_REQUEST);

    }

    // Handle illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleConstraintViolation(IllegalArgumentException ex, WebRequest request) {

        return new ResponseEntity<>(Map.of(
                "error", "Illegal argument provided",
                "message", ex.getMessage()
        ), HttpStatus.BAD_REQUEST);

    }

}
