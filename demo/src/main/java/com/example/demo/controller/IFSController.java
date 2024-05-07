package com.example.demo.controller;
import com.example.demo.entity.FunctionalObject;
import com.example.demo.service.FunctionalObjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class IFSController {
    @Autowired
    FunctionalObjectService functionalObject;

    @PostMapping("/excelUpload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        System.out.println("The file is"+file);
        if(ExcelHelper.hasExcelFormat(file)){
            try {
                List<FunctionalObject> errors=functionalObject.save(file);
                ExcelHelper.writeToExcel(errors);
                String message = "";
                if(errors.size()>0){
                   message = "Errors";
                }else{
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                }
                return new ResponseEntity<>(message, HttpStatus.OK);
            } catch (Exception e) {
                String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    @GetMapping("/excelDownload")
    public ResponseEntity<Resource> downloadExcel() {
        String filePath = "src/main/resources/static/functionalObjectErrors.xls";
        File file = new File(filePath);
        Path path = Paths.get(filePath);
        Resource resource = null;
        try {
            resource = new FileSystemResource(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private void refreshResourceCache() {
        try {
            // Get the path to the static folder
            Path staticFolderPath = Paths.get(getClass().getClassLoader().getResource("static").toURI());

            // Walk through the static folder and mark all files for deletion on exit
            Files.walk(staticFolderPath).forEach(path -> {
                if (!Files.isDirectory(path)) {
                    path.toFile().deleteOnExit();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
