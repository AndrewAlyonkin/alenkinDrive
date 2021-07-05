package net.alenkin.alenkindrive.service;

import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.repository.EventRepository;

import java.util.List;

import static net.alenkin.alenkindrive.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public class EventService {
    private EventRepository repository;

    public Event create(Event event) {
        return repository.save(event);
    }

    public Event update(Event event) {
        return checkNotFoundWithId(repository.save(event), event.getId());
    }

    public Event get(Long id) {
        return checkNotFoundWithId(repository.getOne(id), id);
    }

    public List<Event> getAllByUserId(Long userId) {
        return repository.getAllByUserId(userId);
    }

    public void delete(long id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

}
