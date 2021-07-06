package net.alenkin.alenkindrive.service;

import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.EventRepository;
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

import java.time.LocalDateTime;
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
class EventServiceImplTest {
    @Autowired
    @InjectMocks
    private EventServiceImpl service;

    @MockBean
    private EventRepository repository;

    private ArgumentCaptor<Long> idCaptor;
    private ArgumentCaptor<Long> userIdCaptor;
    private ArgumentCaptor<Event> eventCaptor;

    private Long testId;
    private Long userId;
    private Event testEvent;
    private Event newEvent;
    private Event savedNewEvent;
    private List<Event> testEventsList;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
        testId = 1000L;
        userId = 10000L;
        testEvent = new Event(testId, new StoredFile(), LocalDateTime.of(2021, 6, 7, 10, 0), new User());
        newEvent = new Event(new StoredFile(), LocalDateTime.of(2020, 6, 7, 10, 0), new User());
        savedNewEvent = new Event(1L, new StoredFile(), LocalDateTime.of(2019, 6, 7, 10, 0), new User());
        idCaptor = ArgumentCaptor.forClass(Long.class);
        userIdCaptor = ArgumentCaptor.forClass(Long.class);
        eventCaptor = ArgumentCaptor.forClass(Event.class);
        testEventsList = List.of(testEvent);
    }

    @Test
    void create() {
        Mockito.when(repository.save(newEvent)).thenReturn(savedNewEvent);
        Event saved = service.create(newEvent);
        Mockito.verify(repository).save(eventCaptor.capture());
        assertEquals(newEvent, eventCaptor.getValue());
        assertEquals(savedNewEvent, saved);
    }

    @Test
    void update() {
        Mockito.when(repository.save(testEvent)).thenReturn(testEvent);
        Event current = service.update(testEvent);
        Mockito.verify(repository).save(eventCaptor.capture());
        assertEquals(testEvent, eventCaptor.getValue());
        assertEquals(testEvent, current);
    }

    @Test
    void get() {
        Mockito.when(repository.getByIdAndUserId(testId, userId)).thenReturn(testEvent);
        Event current = service.get(testId, userId);
        Mockito.verify(repository).getByIdAndUserId(idCaptor.capture(), userIdCaptor.capture());
        assertEquals(testId, idCaptor.getValue());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(testEvent, current);
    }

    @Test
    void getAllByUserId() {
        Mockito.when(repository.getAllByUserId(userId)).thenReturn(testEventsList);
        List<Event> currentList = service.getAllByUserId(userId);
        Mockito.verify(repository).getAllByUserId(userIdCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(testEventsList, currentList);
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