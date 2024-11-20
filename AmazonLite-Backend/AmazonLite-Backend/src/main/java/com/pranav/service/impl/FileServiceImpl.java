package com.pranav.service.impl;

import com.pranav.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;

        if(extension.toLowerCase().matches("\\.(png|jpg|jpeg)$")) {
            File folder = new File(path);
            if(!folder.exists()) {
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new RuntimeException("Only .png, .jpg, .jpeg files are allowed.");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path +File.separator + name;

        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
