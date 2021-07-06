package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.service.StoredFileService;
import net.alenkin.alenkindrive.service.UserService;
import net.alenkin.alenkindrive.util.AmazonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static net.alenkin.alenkindrive.util.HttpUtils.buildResponse;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@RestController
@RequestMapping("v1/files/")
public class StoredFileController {
    private final StoredFileService service;

    @Autowired
    private UserService userService;

    @Autowired
    private AmazonUtils amazon;

    @Autowired
    public StoredFileController(StoredFileService service) {
        this.service = service;
    }

    @GetMapping("{userId}/{id}")
    public ResponseEntity<StoredFile> get(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return buildResponse(id, service.get(id, userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<StoredFile> create(@RequestParam(value = "file") MultipartFile file, @PathVariable("userId")Long userId) {
        Long size = file.getSize();
        String url = amazon.uploadFile(file);
        //TODO: set user from security context
        StoredFile savedFile = new StoredFile(url, size, userService.get(userId));

        return buildResponse(file, service.create(savedFile));
    }

    @PutMapping("{userId}/{id}")
    public ResponseEntity<StoredFile> update(@RequestParam(value = "file") MultipartFile file, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        StoredFile savedFile = service.get(id, userId);
        if (savedFile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        amazon.deleteFile(savedFile.getFileURI());
        Long size = file.getSize();
        String url = amazon.uploadFile(file);
        //TODO: set user from security context
        savedFile.setSize(size);
        savedFile.setFileURI(url);
        return buildResponse(file, service.update(savedFile));
    }

    @DeleteMapping(value = "{userId}/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        StoredFile savedFile = service.get(id, userId);
        if (savedFile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        amazon.deleteFile(savedFile.getFileURI());
        service.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<StoredFile>> getAll(@PathVariable("userId") Long userId) {
        List<StoredFile> files = service.getAllByUserId(userId);
        return buildResponse(files, !CollectionUtils.isEmpty(files));
    }
}
