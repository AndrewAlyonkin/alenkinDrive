package net.alenkin.alenkindrive.service;

import net.alenkin.alenkindrive.model.Role;
import net.alenkin.alenkindrive.model.Status;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.UserRepository;
import org.junit.Assert;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    @InjectMocks
    private UserServiceImpl service;

    @MockBean
    private UserRepository repository;

    private ArgumentCaptor<Long> idCaptor;
    private ArgumentCaptor<User> userCaptor;

    private Long testId;
    private User testUser;
    private User newUser;
    private User savedNewUser;
    private List<User> testUsersList;

    @BeforeEach
    public void setUp() {
        Mockito.reset(repository);
        testId = 1000L;
        testUser = new User(testId, "Test");
        newUser = new User("New", Role.USER, Status.ACTIVE, "pass");
        savedNewUser = new User(100L, "Saved");
        idCaptor = ArgumentCaptor.forClass(Long.class);
        userCaptor = ArgumentCaptor.forClass(User.class);
        testUsersList = List.of(testUser);
    }

    @Test
    void create() {
        Mockito.when(repository.save(newUser)).thenReturn(savedNewUser);
        User saved = service.create(newUser);
        Mockito.verify(repository).save(userCaptor.capture());
        assertEquals(newUser, userCaptor.getValue());
        assertEquals(savedNewUser, saved);
    }

    @Test
    void get() {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testUser));
        User current = service.get(testId);
        Mockito.verify(repository).findById(idCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
        assertEquals(testUser, current);
    }

    @Test
    void getAll() {
        Mockito.when(repository.findAll()).thenReturn(testUsersList);
        List<User> currentList = service.getAll();
        Mockito.verify(repository).findAll();
        assertEquals(testUsersList, currentList);
    }

    @Test
    void update() {
        Mockito.when(repository.save(testUser)).thenReturn(testUser);
        User current = service.update(testUser);
        Mockito.verify(repository).save(userCaptor.capture());
        assertEquals(testUser, userCaptor.getValue());
        assertEquals(testUser, current);
    }

    @Test
    void delete() {
        Mockito.when(repository.delete(testId)).thenReturn(1);
        service.delete(testId);
        Mockito.verify(repository).delete(idCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
    }

    @Test
    void deleteNotExist() {
        Mockito.when(repository.delete(testId)).thenReturn(0);
        Assert.assertThrows(IllegalArgumentException.class, () -> service.delete(testId));
        Mockito.verify(repository).delete(idCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
    }
}