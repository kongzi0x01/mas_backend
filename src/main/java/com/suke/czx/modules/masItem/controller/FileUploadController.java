package com.suke.czx.modules.masItem.controller;

import com.suke.czx.common.annotation.AuthIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

    @Value("${staticResourceUrl}")
    private String staticResourceUrl;

    @Value("${staticResourcePath}")
    private String staticResourcePath;

    @AuthIgnore
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            // 创建文件夹如果不存在
            Path uploadDir = Paths.get(staticResourcePath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 构建文件保存路径
            String filename = file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            if(Files.exists(filePath)){
                Files.delete(filePath);
            }
            Files.copy(file.getInputStream(), filePath);

            return ResponseEntity.status(HttpStatus.OK).body(staticResourceUrl + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not store the file. Error: " + e.getMessage());
        }
    }
}
