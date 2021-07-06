package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.service.StoredFileService;
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
class StoredFileControllerTest {

    private ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    private ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
    private String forFile = "newFile";
    private Long ID = 1000L;
    private Long size = 100000L;
    private final StoredFile FILE = new StoredFile(ID, forFile, size, new User(forFile));

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    StoredFileService service;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testContextEnvironment() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("storedFileController"));
    }

    @Test
    void get() throws Exception {
        Mockito.when(service.get(ID, ID)).thenReturn(FILE);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/files/{userId}/{id}", ID, ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileURI").value(forFile))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.name").value(forFile));
    }

    @Test
    void create() throws Exception {
        Mockito.when(service.create(Mockito.any())).thenReturn(FILE);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/files/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileURI").value(forFile))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.name").value(forFile));
    }

    @Test
    void getFail() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/files/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void update() throws Exception {
        Mockito.when(service.update(Mockito.any())).thenReturn(FILE);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/v1/files/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileURI").value(forFile))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.name").value(forFile));
    }

    @Test
    void delete() throws Exception {
        Mockito.doNothing().when(service).delete(ID, ID);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/files/{userId}/{id}", ID, ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(service).delete(idCaptor.capture(), userIdCaptor.capture());
        assertEquals(ID, idCaptor.getValue());
        assertEquals(ID, userIdCaptor.getValue());
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(service.getAllByUserId(ID)).thenReturn(List.of(FILE, FILE));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/files/{userId}/", ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fileURI").value(forFile))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].user.name").value(forFile))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fileURI").value(forFile))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].user.name").value(forFile));
    }
}