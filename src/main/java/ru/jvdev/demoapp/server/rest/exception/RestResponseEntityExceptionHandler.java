package ru.jvdev.demoapp.server.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static ru.jvdev.demoapp.server.rest.exception.RestResponseEntityExceptionHandler.FieldNames.USERNAME;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setHttpStatus(status);
        errorMessage.setMessage(GeneralMessages.INVALID);
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errorMessage.addFieldError(fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage(), fe.getCode());
        }
        return handleExceptionInternal(ex, errorMessage, headers, status, request);
    }

    @ExceptionHandler({UsernameModificationException.class})
    public final ResponseEntity<Object> handleException(UsernameModificationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setHttpStatus(status);
        errorMessage.setMessage(GeneralMessages.INVALID);
        errorMessage.addFieldError(USERNAME, ex.getRejectedValue(), "Username modification is not allowed",
            FieldErrorCodes.NOT_MODIFIABLE);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({UsernameConflictException.class})
    public final ResponseEntity<Object> handleException(UsernameConflictException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setHttpStatus(status);
        errorMessage.setMessage(GeneralMessages.CONFLICT);
        errorMessage.addFieldError(USERNAME, ex.getRejectedValue(), "Username must be unique", FieldErrorCodes.UNIQUE);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({PasswordNotMatchException.class})
    public final ResponseEntity<Object> handleException(PasswordNotMatchException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setHttpStatus(status);
        errorMessage.setMessage("Wrong password");
        errorMessage.addFieldError("currentValue", ex.getRejectedValue(), "Password does not match",
            FieldErrorCodes.NO_MATCH);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
    }

    interface GeneralMessages {
        String INVALID = "Validation failed";
        String CONFLICT = "Conflicting record found";
    }

    interface FieldNames {
        String USERNAME = "username";
    }

    interface FieldErrorCodes {
        String NOT_MODIFIABLE = "NotModifiable";
        String UNIQUE = "Unique";
        String NO_MATCH = "NoMatch";
    }
}
