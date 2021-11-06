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

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle unknown entities
    @ExceptionHandler(UnknownEntityException.class)
    public ResponseEntity<Object> handleNoDataFoundException(UnknownEntityException ex, WebRequest request) {

        return new ResponseEntity<>("The entity was not found", HttpStatus.NOT_FOUND);
    }

    // Handle conflicting entities
    @ExceptionHandler(EntityStateException.class)
    public ResponseEntity<Object> handleNoDataFoundException(EntityStateException ex, WebRequest request) {

        return new ResponseEntity<>("The entity already exists", HttpStatus.NOT_FOUND);
    }

}
