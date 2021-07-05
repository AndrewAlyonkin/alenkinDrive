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
@RequestMapping("/v1/events/")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> get(@PathVariable("id") Long id) {
        return buildResponse(id, service.get(id));
    }

    @PostMapping("")
    public ResponseEntity<Event> create(Event event) {
        return buildResponse(event, service.create(event));
    }

    @PutMapping("")
    public ResponseEntity<Event> update(Event event) {
        return buildResponse(event, service.update(event));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Event> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<Event>> getAll(@PathVariable("id") Long id) {
        List<Event> events = service.getAllByUserId(id);
        return buildResponse(events, !CollectionUtils.isEmpty(events));
    }
}
