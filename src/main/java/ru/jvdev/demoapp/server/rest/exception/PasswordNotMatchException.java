package ru.jvdev.demoapp.server.rest.exception;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 08.12.2016
 */
public class PasswordNotMatchException extends RuntimeException {
    private String rejectedValue;

    public PasswordNotMatchException(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
