package ru.jvdev.demoapp.server.rest;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestDataCreator creator;

    private MockMvc mockMvc;

    private int employeeId;

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
        employeeId = creator.createEmployee(FIRSTNAME, LASTNAME, USERNAME);
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
}
