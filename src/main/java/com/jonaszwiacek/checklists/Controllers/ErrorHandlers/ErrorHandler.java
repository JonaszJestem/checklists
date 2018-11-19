package com.jonaszwiacek.checklists.Controllers.ErrorHandlers;

import com.jonaszwiacek.checklists.Services.Exceptions.ChecklistAlreadyExistsException;
import com.jonaszwiacek.checklists.Services.Exceptions.ChecklistNotFoundException;
import com.jonaszwiacek.checklists.Services.Exceptions.ItemNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice("com.jonaszwiacek.checklists")
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ChecklistAlreadyExistsException.class)
    public final ResponseEntity<ErrorDetails> handleAlreadyExists(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ChecklistNotFoundException.class, ItemNotFoundException.class})
    public final ResponseEntity<ErrorDetails> handleNotFoundException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("We don't support that feature at this moment, sorry!");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_IMPLEMENTED);
    }
}
