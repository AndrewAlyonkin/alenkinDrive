package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.alenkin.alenkindrive.util.HttpUtil.buildResponse;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@RestController
@RequestMapping("v1/events/")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("{userId}/{id}")
    public ResponseEntity<Event> get(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return buildResponse(id, service.get(id, userId));
    }

    @PostMapping("")
    public ResponseEntity<Event> create(Event event) {
        return buildResponse(event, service.create(event));
    }

    @PutMapping("")
    public ResponseEntity<Event> update(Event event) {
        return buildResponse(event, service.update(event));
    }

    @DeleteMapping(value = "{userId}/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        service.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<Event>> getAll(@PathVariable("userId") Long userId) {
        List<Event> events = service.getAllByUserId(userId);
        return buildResponse(events, !CollectionUtils.isEmpty(events));
    }
}
