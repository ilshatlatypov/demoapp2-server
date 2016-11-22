package ru.jvdev.demoapp.server.rest;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ru.jvdev.demoapp.server.dto.EmployeeDTO;
import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Role;
import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 21.11.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeRestServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private EmployeeRepository employeeRepository;

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
    }

    @Test
    public void create() throws Exception {
        EmployeeDTO emp = new EmployeeDTO();
        emp.setFirstname("Firstname");
        emp.setLastname("Lastname");
        emp.setUsername("username");

        String empJson = json(emp);

        mockMvc.perform(post("/rest/employees").contentType(MediaType.APPLICATION_JSON).content(empJson))
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void update() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRole(Role.WORKER);

        Employee emp = new Employee();
        emp.setFirstname("Firstname");
        emp.setLastname("Lastname");
        emp.setUser(user);

        int empId = employeeRepository.save(emp).getId();

        EmployeeDTO empForUpdate = new EmployeeDTO();
        empForUpdate.setFirstname("Firstname - Updated");
        empForUpdate.setLastname("Lastname - Updated");
        empForUpdate.setUsername("username");

        String empForUpdateJson = json(empForUpdate);

        mockMvc.perform(put("/rest/employees/" + empId).contentType(MediaType.APPLICATION_JSON).content(empForUpdateJson))
            .andExpect(status().isOk());

        Employee persisted = employeeRepository.getOne(empId);
        assertEquals("Firstname - Updated", persisted.getFirstname());
        assertEquals("Lastname - Updated", persisted.getLastname());
    }

    @SuppressWarnings("unchecked")
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}