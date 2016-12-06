package ru.jvdev.demoapp.server.dto.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import ru.jvdev.demoapp.server.dto.EmployeeDTO;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 21.11.2016
 */
public class EmployeeDTOValidationTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();

    }

    @Test
    public void validEmployee() {
        EmployeeDTO validEmployee = getValid();
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(validEmployee);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void employeeWithBlankFirstname() {
        EmployeeDTO emp = getValid();
        emp.setFirstname("");
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void employeeWithBlankLastname() {
        EmployeeDTO emp = getValid();
        emp.setLastname("");
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void employeeWithBlankUsername() {
        EmployeeDTO emp = getValid();
        emp.setUsername("");
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void employeeWithNonLetterCharacterInUsername() {
        EmployeeDTO emp = getValid();
        emp.setUsername("user_name");
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void employeeWithCapitalCharacterInUsername() {
        EmployeeDTO emp = getValid();
        emp.setUsername("userName");
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void employeeWithTooShortUsername() throws Exception {
        EmployeeDTO emp = getValid();
        emp.setUsername("us");
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void employeeWithTooLongUsername() throws Exception {
        EmployeeDTO emp = getValid();
        emp.setUsername("usernameusernameusern");
        assertTrue(emp.getUsername().length() > EmployeeDTO.MAX_USERNAME_LENGTH);
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(emp);
        assertFalse(violations.isEmpty());
    }

    private EmployeeDTO getValid() {
        EmployeeDTO emp = new EmployeeDTO();
        emp.setFirstname("Firstname");
        emp.setLastname("Lastname");
        emp.setUsername("username");
        return emp;
    }
}