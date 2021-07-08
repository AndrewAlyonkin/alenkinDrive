package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.security.UserDetailsServiceImpl;
import net.alenkin.alenkindrive.service.StoredFileService;
import net.alenkin.alenkindrive.service.UserService;
import net.alenkin.alenkindrive.util.AmazonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<ByteArrayResource> get(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        StoredFile file = service.getByIdAndUserId(id, userId);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String filleURI = file.getFileURI();
        byte[] data = amazon.downloadFile(filleURI);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + filleURI + "\"")
                .body(resource);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<StoredFile> create(@RequestParam(value = "file") MultipartFile file, @PathVariable("userId")Long userId) {
        long size = file.getSize();
        String url = amazon.uploadFile(file); //TODO
        StoredFile savedFile = new StoredFile(url, size, userService.get(UserDetailsServiceImpl.getAuthUserId()));

        return buildResponse(file, service.create(savedFile));
    }

    @PutMapping("{userId}/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<StoredFile> update(@RequestParam(value = "file") MultipartFile file, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        StoredFile savedFile = service.getByIdAndUserId(id, userId);
        if (savedFile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        amazon.deleteFile(savedFile.getFileURI());
        long size = file.getSize();
        String url = amazon.uploadFile(file);  //TODO
        savedFile.setUser(userService.get(UserDetailsServiceImpl.getAuthUserId()));
        savedFile.setSize(size);
        savedFile.setFileURI(url);
        return buildResponse(file, service.update(savedFile));
    }

    @DeleteMapping(value = "{userId}/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        StoredFile savedFile = service.getByIdAndUserId(id, userId);
        if (savedFile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        amazon.deleteFile(savedFile.getFileURI());
        service.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<List<StoredFile>> getAll(@PathVariable("userId") Long userId) {
        List<StoredFile> files = service.getAllByUserId(userId);
        return buildResponse(files, !CollectionUtils.isEmpty(files));
    }
}
