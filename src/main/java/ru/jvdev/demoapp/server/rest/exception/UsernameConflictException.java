package ru.jvdev.demoapp.server.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 22.11.2016
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Username must be unique")
public class UsernameConflictException extends RuntimeException {
    private String rejectedValue;

    public UsernameConflictException(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
