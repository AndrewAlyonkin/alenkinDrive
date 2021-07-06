package net.alenkin.alenkindrive.service;

import lombok.extern.slf4j.Slf4j;
import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.alenkin.alenkindrive.util.ValidationUtils.checkNotFoundWithId;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Autowired
    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    public Event create(Event event) {
        log.info("Create new event");
        return repository.save(event);
    }

    public Event update(Event event) {
        long eventId = event.getId();
        log.info("Update event id = {}",eventId);
        return checkNotFoundWithId(repository.save(event), eventId);
    }

    public Event get(Long id, Long userId) {
        log.info("Get event id = {}", id);
        return repository.getByIdAndUserId(id, userId);
    }

    public List<Event> getAllByUserId(Long userId) {
        log.info("get all events with iser id = {}", userId);
        return repository.getAllByUserId(userId);
    }

    public void delete(long id, Long userId) {
        log.info("Delete event id = {}", id);
        checkNotFoundWithId(repository.deleteByIdAndUserId(id, userId) != 0, id);
    }

}
