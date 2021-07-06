package net.alenkin.alenkindrive.service;

import net.alenkin.alenkindrive.model.Event;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public interface EventService {
    Event create(Event event);

    Event update(Event event);

    Event get(Long id, Long userId);

    List<Event> getAllByUserId(Long userId);

    void delete(long id, Long userId);
}
