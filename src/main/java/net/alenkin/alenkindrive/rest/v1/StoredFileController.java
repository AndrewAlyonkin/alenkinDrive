package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.service.StoredFileService;
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
@RequestMapping("v1/files/")
public class StoredFileController {
    private final StoredFileService service;

    @Autowired
    public StoredFileController(StoredFileService service) {
        this.service = service;
    }

    @GetMapping("{userId}/{id}")
    public ResponseEntity<StoredFile> get(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return buildResponse(id, service.get(id, userId));
    }

    @PostMapping("")
    public ResponseEntity<StoredFile> create(StoredFile file) {
        return buildResponse(file, service.create(file));
    }

    @PutMapping("")
    public ResponseEntity<StoredFile> update(StoredFile file) {
        return buildResponse(file, service.update(file));
    }

    @DeleteMapping(value = "{userId}/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        service.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<StoredFile>> getAll(@PathVariable("userId") Long userId) {
        List<StoredFile> files = service.getAllByUserId(userId);
        return buildResponse(files, !CollectionUtils.isEmpty(files));
    }
}
