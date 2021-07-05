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
@RequestMapping("/v1/files/")
public class StoredFileController {
    private final StoredFileService service;

    @Autowired
    public StoredFileController(StoredFileService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<StoredFile> get(@PathVariable("id") Long id) {
        return buildResponse(id, service.get(id));
    }

    @PostMapping("")
    public ResponseEntity<StoredFile> create(StoredFile file) {
        return buildResponse(file, service.create(file));
    }

    @PutMapping("")
    public ResponseEntity<StoredFile> update(StoredFile file) {
        return buildResponse(file, service.update(file));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<StoredFile> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<StoredFile>> getAll(@PathVariable("id") Long id) {
        List<StoredFile> files = service.getAllByUserId(id);
        return buildResponse(files, !CollectionUtils.isEmpty(files));
    }
}
