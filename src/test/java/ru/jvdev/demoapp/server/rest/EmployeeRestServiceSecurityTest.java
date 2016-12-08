package ru.jvdev.demoapp.server.rest;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.jvdev.demoapp.server.dto.PasswordDTO;
import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;
import ru.jvdev.demoapp.server.repository.UserRepository;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 08.12.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeRestServiceSecurityTest {

    private static final String FIRSTNAME = "Harry";
    private static final String LASTNAME = "Potter";
    private static final String USERNAME = "hpotter";
    private static final String PASSWORD = "secretPassword";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestDataCreator creator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    private int employeeId;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        //noinspection OptionalGetWithoutIsPresent
        mappingJackson2HttpMessageConverter = Arrays.stream(converters)
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny().get();
        assertNotNull("the JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }

    /**
     * This must be done in @Before method, but had to put it to @PostConstruct because of <a href="
     * http://stackoverflow.com/questions/38275420/issue-with-withuserdetails-and-spring-boot-1-4-testentitymanager
     * ">the issue in Spring Boot</a>.<br/>
     * Annotation @WithUserDetails works before @Before method, when no test data is prepared.
     */
    @PostConstruct
    public void prepareData() {
        employeeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        employeeId = creator.createEmployee(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
    }

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @WithUserDetails(value = USERNAME)
    public void testGetProfile() throws Exception {
        mockMvc.perform(get("/rest/employees/profile"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(employeeId)))
            .andExpect(jsonPath("$.firstname", is(FIRSTNAME)))
            .andExpect(jsonPath("$.lastname", is(LASTNAME)))
            .andExpect(jsonPath("$.username", is(USERNAME)));
    }

    @Test
    @WithUserDetails(value = USERNAME)
    public void testChangePassword() throws Exception {
        final String newPasswordValue = "newPasswordValue";

        PasswordDTO password = new PasswordDTO();
        password.setCurrentValue(PASSWORD);
        password.setNewValue(newPasswordValue);

        String passwordJson = json(password);
        mockMvc.perform(changePasswordRequest(passwordJson))
            .andExpect(status().isOk());

        Employee storedEmployee = employeeRepository.findOne(employeeId);
        String storedPasswordValue = storedEmployee.getUser().getPassword();
        assertTrue(passwordEncoder.matches(newPasswordValue, storedPasswordValue));
    }

    @Test
    @WithUserDetails(value = USERNAME)
    public void testChangePasswordFailsIfWrongCurrentPasswordProvided() throws Exception {
        final String wrongCurrentValue = "wrongCurrentValue";
        final String newPasswordValue = "newPasswordValue";

        PasswordDTO password = new PasswordDTO();
        password.setCurrentValue(wrongCurrentValue);
        password.setNewValue(newPasswordValue);

        String passwordJson = json(password);
        mockMvc.perform(changePasswordRequest(passwordJson))
            .andExpect(status().isBadRequest());
    }

    private static MockHttpServletRequestBuilder changePasswordRequest(String passwordJson) {
        return post("/rest/employees/changePassword").contentType(MediaType.APPLICATION_JSON).content(passwordJson);
    }

    @SuppressWarnings("unchecked")
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
