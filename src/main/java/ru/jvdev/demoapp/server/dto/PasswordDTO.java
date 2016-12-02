package ru.jvdev.demoapp.server.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 02.12.2016
 */
public class PasswordDTO {
    @Getter
    @Setter
    @Length(min = 3)
    private String value;
}
