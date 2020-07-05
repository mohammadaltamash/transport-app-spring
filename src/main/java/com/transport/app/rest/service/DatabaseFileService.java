package com.transport.app.rest.service;

import com.transport.app.rest.domain.DatabaseFile;
import com.transport.app.rest.repository.DatabaseFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DatabaseFileService {

    @Autowired
    private DatabaseFileRepository repository;

    public DatabaseFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), file.getBytes());
            return repository.save(dbFile);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public DatabaseFile getFile(Long fileId) {
        return repository.findById(fileId).orElseThrow(() -> new RuntimeException("File with id " + fileId + " not found"));
    }

    public List<DatabaseFile> getAllFilesData() {
        return repository.findAll();
    }
}
