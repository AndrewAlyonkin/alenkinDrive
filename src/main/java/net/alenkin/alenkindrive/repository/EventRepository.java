package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> getAllByUserId(Long userId);
}
