package net.alenkin.alenkindrive.service;

import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.StoredFileRepository;
import net.alenkin.alenkindrive.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
class StorageFileServiceImplTest {
    @Autowired
    @InjectMocks
    private StorageFileServiceImpl service;

    @MockBean
    private StoredFileRepository repository;


    @MockBean
    private EventService eventService;

    private ArgumentCaptor<Long> idCaptor;
    private ArgumentCaptor<Long> userIdCaptor;
    private ArgumentCaptor<StoredFile> fileCaptor;

    private Long testId;
    private Long userId;
    private StoredFile testFile;
    private StoredFile newFile;
    private StoredFile savedNewFile;
    private List<StoredFile> testFilesList;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
        testId = 1000L;
        userId = 10000L;
        testFile = new StoredFile(1000L, "Chuck://test/testDir/testFile.pdf", 9000, new User());
        newFile = new StoredFile("NEW", 9000, new User());
        savedNewFile = new StoredFile(1L, "NEW", 9000, new User());
        idCaptor = ArgumentCaptor.forClass(Long.class);
        userIdCaptor = ArgumentCaptor.forClass(Long.class);
        fileCaptor = ArgumentCaptor.forClass(StoredFile.class);
        testFilesList = List.of(testFile);
    }

    @Test
    void create() {
        Mockito.when(repository.save(newFile)).thenReturn(savedNewFile);
        StoredFile saved = service.create(newFile);
        Mockito.verify(repository).save(fileCaptor.capture());
        Assertions.assertEquals(newFile, fileCaptor.getValue());
        Assertions.assertEquals(savedNewFile, saved);
    }

    @Test
    void update() {
        Mockito.when(repository.save(testFile)).thenReturn(testFile);
        StoredFile current = service.update(testFile);
        Mockito.verify(repository).save(fileCaptor.capture());
        Assertions.assertEquals(testFile, fileCaptor.getValue());
        Assertions.assertEquals(testFile, current);
    }

    @Test
    void get() {
        Mockito.when(eventService.create(Mockito.any())).thenReturn(new Event());
        Mockito.when(repository.getByIdAndUserId(testId, userId)).thenReturn(testFile);
        StoredFile current = service.getByIdAndUserId(testId, userId);
        Mockito.verify(repository).getByIdAndUserId(idCaptor.capture(), userIdCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(testFile, current);
    }

    @Test
    void getAllByUserId() {
        Mockito.when(repository.getAllByUserId(userId)).thenReturn(testFilesList);
        List<StoredFile> currentList = service.getAllByUserId(userId);
        Mockito.verify(repository).getAllByUserId(userIdCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(testFilesList, currentList);
    }

    @Test
    void delete() {
        Mockito.when(repository.deleteByIdAndUserId(testId, userId)).thenReturn(1);
        service.delete(testId, userId);
        Mockito.verify(repository).deleteByIdAndUserId(idCaptor.capture(), userIdCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
        assertEquals(userId, userIdCaptor.getValue());
    }

    @Test
    void deleteNotExist() {
        Mockito.when(repository.deleteByIdAndUserId(testId, userId)).thenReturn(0);
        assertThrows(IllegalArgumentException.class, () -> service.delete(testId, userId));
        Mockito.verify(repository).deleteByIdAndUserId(idCaptor.capture(), userIdCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
        assertEquals(userId, userIdCaptor.getValue());
    }
}