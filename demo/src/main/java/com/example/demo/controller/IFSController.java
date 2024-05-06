package com.example.demo.controller;
import com.example.demo.entity.FunctionalObject;
import com.example.demo.service.FunctionalObjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } catch (Exception e) {
                String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }
}
