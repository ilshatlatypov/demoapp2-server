package ru.jvdev.demoapp.server.rest;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.jvdev.demoapp.server.dto.EmployeeDTO;
import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Role;
import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;
import ru.jvdev.demoapp.server.repository.UserRepository;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 21.11.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeRestServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        //noinspection OptionalGetWithoutIsPresent
        mappingJackson2HttpMessageConverter = Arrays.stream(converters)
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny().get();
        assertNotNull("the JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        employeeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    public void create() throws Exception {
        EmployeeDTO employee = buildEmployeeDTO("Firstname", "Lastname", "username");
        String employeeJson = json(employee);
        mockMvc.perform(createEmployeeRequest(employeeJson))
            .andExpect(status().isCreated());
    }

    @Test
    public void createFailsIfUsernameNotUnique() throws Exception {
        createEmployee("Michael", "Scott", "mscott");

        EmployeeDTO employee = buildEmployeeDTO("James", "Halpert", "mscott");
        String employeeJson = json(employee);
        mockMvc.perform(createEmployeeRequest(employeeJson))
            .andExpect(status().isConflict());
    }

    @Test
    public void update() throws Exception {
        int employeeId = createEmployee("Michael", "Scott", "mscott");

        EmployeeDTO employeeForUpdate = buildEmployeeDTO("Mike", "Scotty", "mscott");
        String employeeForUpdateJson = json(employeeForUpdate);
        mockMvc.perform(updateEmployeeRequest(employeeId, employeeForUpdateJson))
            .andExpect(status().isOk());

        Employee persisted = employeeRepository.findOne(employeeId);
        assertEquals("Mike", persisted.getFirstname());
        assertEquals("Scotty", persisted.getLastname());
    }

    @Test
    public void updateFailsOnUsernameModificationAttempt() throws Exception {
        int employeeId = createEmployee("Michael", "Scott", "mscott");

        EmployeeDTO employeeForUpdate = buildEmployeeDTO("Michael", "Scott", "michael");
        String employeeForUpdateJson = json(employeeForUpdate);
        mockMvc.perform(updateEmployeeRequest(employeeId, employeeForUpdateJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getEmployee() throws Exception {
        int employeeId = createEmployee("Michael", "Scott", "mscott");

        mockMvc.perform(get("/rest/employees/" + employeeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(employeeId)))
            .andExpect(jsonPath("$.firstname", is("Michael")))
            .andExpect(jsonPath("$.lastname", is("Scott")))
            .andExpect(jsonPath("$.username", is("mscott")));
    }

    @Test
    public void gettingNonExistingEmployeeRespondsWithNotFound() throws Exception {
        int nonExistingId = 1;
        assertFalse(employeeRepository.exists(nonExistingId));
        mockMvc.perform(get("/rest/employees/" + nonExistingId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployee() throws Exception {
        int employeeId = createEmployee("Michael", "Scott", "mscott");
        mockMvc.perform(delete("/rest/employees/" + employeeId))
            .andExpect(status().isNoContent());
        assertFalse(employeeRepository.exists(employeeId));
    }

    @Test
    public void deletingNonExistingEmployeeRespondsWithNotFound() throws Exception {
        int nonExistingId = 1;
        assertFalse(employeeRepository.exists(nonExistingId));
        mockMvc.perform(delete("/rest/employees/" + nonExistingId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testUsernameUsed() throws Exception {
        String username = "mscott";
        createEmployee("Michael", "Scott", username);
        mockMvc.perform(get("/rest/employees/isUsernameAvailable/" + username))
            .andExpect(status().isConflict());
    }

    @Test
    public void testUsernameAvailable() throws Exception {
        String username = "notused";
        mockMvc.perform(get("/rest/employees/isUsernameAvailable/" + username))
            .andExpect(status().isOk());
    }

    private static MockHttpServletRequestBuilder createEmployeeRequest(String employeeJson) {
        return post("/rest/employees").contentType(MediaType.APPLICATION_JSON).content(employeeJson);
    }

    private static MockHttpServletRequestBuilder updateEmployeeRequest(int id, String employeeForUpdateJson) {
        return put("/rest/employees/" + id).contentType(MediaType.APPLICATION_JSON).content(employeeForUpdateJson);
    }

    private int createEmployee(String firstname, String lastname, String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(username);
        user.setEnabled(true);
        user.setRole(Role.WORKER);

        Employee emp = new Employee();
        emp.setFirstname(firstname);
        emp.setLastname(lastname);
        emp.setUser(user);

        return employeeRepository.save(emp).getId();
    }

    private static EmployeeDTO buildEmployeeDTO(String firstname, String lastname, String username) {
        EmployeeDTO emp = new EmployeeDTO();
        emp.setFirstname(firstname);
        emp.setLastname(lastname);
        emp.setUsername(username);
        return emp;
    }

    @SuppressWarnings("unchecked")
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}