package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static net.alenkin.alenkindrive.util.HttpUtil.buildResponse;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@RestController
@RequestMapping("v1/users/")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<User> get(@PathVariable("id") Long id) {
        return buildResponse(id, service.get(id));
    }

    @PostMapping("")
    public ResponseEntity<User> create(User user) {
        return buildResponse(user, service.create(user));
    }

    @PutMapping("")
    public ResponseEntity<User> update(User user) {
        return buildResponse(user, service.update(user));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = service.getAll();
        return buildResponse(users, !CollectionUtils.isEmpty(users));
    }
}
