package ru.jvdev.demoapp.server.rest.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

class ErrorMessage {
    private int status;
    private String error;
    private List<FieldError> errors;
    private String message;

    ErrorMessage() {
        errors = new ArrayList<>();
    }

    void addFieldError(String field, Object rejectedValue, String defaultMessage) {
        errors.add(new FieldError(field, rejectedValue, defaultMessage));
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    void setHttpStatus(HttpStatus status) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
    }


    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Light version of {@link org.springframework.validation.FieldError}
     */
    @Data
    @AllArgsConstructor
    private static class FieldError {
        private String field;
        private Object rejectedValue;
        private String message;
    }
}
