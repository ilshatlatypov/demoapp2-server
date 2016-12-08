package ru.jvdev.demoapp.server.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 02.12.2016
 */
public class PasswordDTO {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    @Getter
    @Setter
    @NotBlank
    private String currentValue;
    @Getter
    @Setter
    @Length(min = MIN_LENGTH, max = MAX_LENGTH)
    private String newValue;
}
