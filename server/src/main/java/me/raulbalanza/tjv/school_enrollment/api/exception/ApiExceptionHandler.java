package me.raulbalanza.tjv.school_enrollment.api.exception;

import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

}
