package ru.jvdev.demoapp.server.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 22.11.2016
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Username modification is not allowed")
public class UsernameModificationException extends RuntimeException {
}
