package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class UserControllerTest {

    private Long ID = 1000L;
    private ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    UserService service;

    private final String forUser = "TEST";
    private final User USER = new User(ID, forUser);

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testContextEnvironment() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("userController"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get() throws Exception {
        Mockito.when(service.get(ID)).thenReturn(USER);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/users/{id}", ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(forUser));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create() throws Exception {
        Mockito.when(service.create(Mockito.any())).thenReturn(USER);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/users/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(forUser));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update() throws Exception {
        Mockito.when(service.update(Mockito.any())).thenReturn(USER);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/v1/users/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(forUser));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete() throws Exception {
        Mockito.doNothing().when(service).delete(ID);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/users/{userId}", ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(service).delete(idCaptor.capture());
        assertEquals(ID, idCaptor.getValue());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll() throws Exception {
        Mockito.when(service.getAll()).thenReturn(List.of(USER, USER));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/users/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(forUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(forUser));
    }

    //SECURITY TESTS
    @Test
    @WithMockUser(roles = {"USER", "MODERATOR"})
    void getAllDenied() {
       assertThrows(org.springframework.web.util.NestedServletException.class,
               this::getAll);
    }

    @Test
    @WithMockUser(roles = {"USER", "MODERATOR"})
    void getDenied() {
        assertThrows(org.springframework.web.util.NestedServletException.class,
                this::get);
    }

    @Test
    @WithMockUser(roles = {"USER", "MODERATOR"})
    void createDenied() {
        assertThrows(org.springframework.web.util.NestedServletException.class,
                this::create);
    }

    @Test
    @WithMockUser(roles = {"USER", "MODERATOR"})
    void updateDenied() {
        assertThrows(org.springframework.web.util.NestedServletException.class,
                this::update);
    }

    @Test
    @WithMockUser(roles = {"USER", "MODERATOR"})
    void deleteDenied() {
        assertThrows(org.springframework.web.util.NestedServletException.class,
                this::delete);
    }
}