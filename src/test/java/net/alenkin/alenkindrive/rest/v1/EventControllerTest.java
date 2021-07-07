package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.service.EventService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class EventControllerTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final Long ID = 500L;
    private final String forEventName = "test";
    private final LocalDateTime dateTime = LocalDateTime.of(2021, 6, 7, 10, 0, 0);
    private final long size = 5000L;

    private ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    private ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

    private final Event EVENT = new Event(ID, new StoredFile(forEventName, size, new User(forEventName)),
            dateTime, new User(forEventName));


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    EventService service;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testContextEnvironment() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("eventController"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR", "USER"})
    void get() throws Exception {
        Mockito.when(service.get(ID, ID)).thenReturn(EVENT);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/events/{userId}/{id}", ID, ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.downloadDateTime").value(dateTime.format(formatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.name").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.fileURI").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.user.name").value(forEventName));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR", "USER"})
    void getFail() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/events/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    void create() throws Exception {
//        Mockito.when(service.create(Mockito.any())).thenReturn(EVENT);
//        this.mockMvc
//                .perform(MockMvcRequestBuilders.post("/v1/events/")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.downloadDateTime").value(dateTime.format(formatter)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user.name").value(forEventName))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.fileURI").value(forEventName))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.size").value(size))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.user.name").value(forEventName));
//    }

//    @Test
//    void update() throws Exception {
//        Mockito.when(service.update(Mockito.any())).thenReturn(EVENT);
//        this.mockMvc
//                .perform(MockMvcRequestBuilders.put("/v1/events/{userId}/{id}", ID, ID)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.downloadDateTime").value(dateTime.format(formatter)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user.name").value(forEventName))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.fileURI").value(forEventName))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.size").value(size))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.storedFile.user.name").value(forEventName));
//    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete() throws Exception {
        Mockito.doNothing().when(service).delete(ID, ID);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/events/{userId}/{id}", ID, ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(service).delete(idCaptor.capture(), userIdCaptor.capture());
        assertEquals(ID, idCaptor.getValue());
        assertEquals(ID, userIdCaptor.getValue());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR", "USER"})
    void getAll() throws Exception {
        Mockito.when(service.getAllByUserId(ID)).thenReturn(List.of(EVENT, EVENT));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/events/{userId}", ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].downloadDateTime").value(dateTime.format(formatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].user.name").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].storedFile.fileURI").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].storedFile.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].storedFile.user.name").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].downloadDateTime").value(dateTime.format(formatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].user.name").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].storedFile.fileURI").value(forEventName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].storedFile.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].storedFile.user.name").value(forEventName));
    }

    //SECURITY TESTS
    @WithMockUser(roles = {"MODERATOR", "USER"})
    @Test
    void deleteDenied() {
        assertThrows(org.springframework.web.util.NestedServletException.class,
                this::delete);
    }
}