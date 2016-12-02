package ru.jvdev.demoapp.server.rest.exception;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 22.11.2016
 */
public class UsernameModificationException extends RuntimeException {
    private String rejectedValue;

    public UsernameModificationException(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
