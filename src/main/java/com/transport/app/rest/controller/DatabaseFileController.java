package com.transport.app.rest.controller;

import com.transport.app.rest.domain.DatabaseFile;
import com.transport.app.rest.domain.FileResponse;
import com.transport.app.rest.service.DatabaseFileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class DatabaseFileController {

    private DatabaseFileService fileService;

    DatabaseFileController(DatabaseFileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/{orderId}/{location}/{timestamp}")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file,
                                   @PathVariable Long orderId,
                                   @PathVariable String location,
                                   @PathVariable String timestamp) {
        DatabaseFile fileName = fileService.storeFile(file, orderId, location, timestamp);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getId().toString())
                .toUriString();
        return new FileResponse(fileName.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
    }
    @PostMapping("/upload/{orderId}/{location}/{marking}/{timestamp}")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file,
                                   @PathVariable Long orderId,
                                   @PathVariable String location,
                                   @PathVariable Boolean marking,
                                   @PathVariable String timestamp) {
        DatabaseFile fileName = fileService.storeMarkingFile(file, orderId, location, timestamp);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getId().toString())
                .toUriString();
        return new FileResponse(fileName.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles/{orderId}/{location}/{timestamp}")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                  @PathVariable Long orderId,
                                                  @PathVariable String location,
                                                  @PathVariable String timestamp) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, orderId, location, timestamp))
                .collect(Collectors.toList());
    }

    @PostMapping("/upload/{orderId}/{signedBy}")
    public FileResponse uploadSignFile(@RequestParam("file") MultipartFile file,
                                   @PathVariable Long orderId,
                                   @PathVariable String signedBy) throws IOException {
//        byte[] data = new byte[0];
//        file.getInputStream().read(data);
//        return null;
        DatabaseFile fileName = fileService.storeSignatureFileAndUpdateOrderStatus(file, orderId, signedBy);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getId().toString())
                .toUriString();
        return new FileResponse(fileName.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileName, HttpServletRequest request) {
        // Load file as Resource
        DatabaseFile databaseFile = fileService.getFile(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }

    @GetMapping("/files")
    public List<DatabaseFile> getAllFilesData() {
        return fileService.getAllFilesData();
    }

    @GetMapping("/files/{type}")
    public List<String> getAllFileUriWithFileType(@PathVariable String type, HttpServletRequest request) {
        return fileService.getAllFileUriWithFileType(type,
                request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath() + "/downloadFile/");
    }

    @GetMapping("/files/{fileType}/{orderId}/{location}")
    public List<String> getByOrderIdAndLocation(@PathVariable String fileType, @PathVariable Long orderId, @PathVariable String location, HttpServletRequest request) {
        return fileService.getByOrderIdAndLocation(fileType,
                request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath() + "/downloadFile/",
                orderId, location);
    }
    /*@GetMapping("/files/{fileType}/{orderId}/{location}/{marking}")
    public List<String> getByOrderIdAndLocation(@PathVariable String fileType, @PathVariable Long orderId, @PathVariable String location,  @PathVariable Boolean marking,
                                                HttpServletRequest request) {
        return fileService.getByOrderIdAndLocation(fileType,
                request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath() + "/downloadFile/",
                orderId, location);
    }*/

    @GetMapping("/files/{orderId}/{signedBy}")
    public String getByOrderIdAndSignedBy(@PathVariable Long orderId, @PathVariable String signedBy, HttpServletRequest request) {
        return fileService.getByOrderIdAndSignedBy(
                request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath() + "/downloadFile/",
                orderId, signedBy);
    }

    @GetMapping("/files/marking/{orderId}/{location}")
    public List<String> getByOrderIdLocationAndMarking(@PathVariable Long orderId, @PathVariable String location, HttpServletRequest request) {
        return fileService.getByOrderIdLocationAndMarking(
                request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath() + "/downloadFile/",
                orderId, location);
    }
}
