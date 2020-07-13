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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
                .path(fileName.getFileName())
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
}
