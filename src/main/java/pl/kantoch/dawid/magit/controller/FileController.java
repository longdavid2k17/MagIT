package pl.kantoch.dawid.magit.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import pl.kantoch.dawid.magit.models.payloads.responses.FileInfo;
import pl.kantoch.dawid.magit.services.file_service.FilesStorageService;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FilesStorageService storageService;

    public FileController(FilesStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload-result/{taskId}")
    public ResponseEntity<?> uploadResult(@PathVariable Long taskId,@RequestHeader("Authorization")String token ,@RequestParam("file") MultipartFile file) {
        if(taskId==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie przekazano parametru zadania"));
        String message = "";
        try {
            storageService.save(file,taskId,token);
            message = "Pomyślnie przesłano plik: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(GsonInstance.get().toJson(message));
        } catch (Exception e) {
            message = "Wystąpił błąd podczas przesyłania pliku! "+e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(GsonInstance.get().toJson(message));
        }
    }

    @PostMapping("/upload-attachment/{taskId}")
    public ResponseEntity<?> uploadAttachment(@PathVariable Long taskId,@RequestHeader("Authorization")String token ,@RequestParam("file") MultipartFile file) {
        if(taskId==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie przekazano parametru zadania"));
        String message = "";
        try {
            storageService.saveAttachment(file,taskId,token);
            message = "Pomyślnie przesłano plik: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(GsonInstance.get().toJson(message));
        } catch (Exception e) {
            message = "Wystąpił błąd podczas przesyłania pliku! "+e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(GsonInstance.get().toJson(message));
        }
    }

    @GetMapping("/result-files/{taskId}")
    public ResponseEntity<?> getListFiles(@PathVariable Long taskId) {
        try {
            List<FileInfo> fileInfos = storageService.loadAllResultsFile(taskId).stream().map(path -> {
                String filename = path.getFileName().toString();
                String url = MvcUriComponentsBuilder
                        .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();
                return new FileInfo(filename, url);
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
        }
        catch (Exception e){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson(e.getMessage()));
        }
    }

    @GetMapping("/attachment-files/{taskId}")
    public ResponseEntity<?> getAttachmentListFiles(@PathVariable Long taskId) {
        try {
            List<FileInfo> fileInfos = storageService.loadAllAttachments(taskId).stream().map(path -> {
                String filename = path.getFileName().toString();
                String url = MvcUriComponentsBuilder
                        .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();
                return new FileInfo(filename, url);
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson(e.getMessage()));
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
