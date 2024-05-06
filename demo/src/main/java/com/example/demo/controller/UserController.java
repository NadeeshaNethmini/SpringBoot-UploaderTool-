package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    public List<UserDTO> getUser(){
        return userService.getAllUsers();
    }

    @PostMapping("/saveUser")
    public UserDTO saveUser(@RequestBody UserDTO userDTO){
        System.out.println(userDTO);
//        return "User Saved";
        return userService.saveUser(userDTO);
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        System.out.println(file.getOriginalFilename());
        String rootDirectory = System.getProperty("user.dir");

        // Example paths for saving images and videos
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads";

        try {
            String fileName = file.getOriginalFilename();
            Path policeReportFilePath = Paths.get(imageUploadPath, fileName);
            Files.write(policeReportFilePath, file.getBytes());
            return "File uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("Name") String name){
        System.out.println(name);
        return "File uploaded successfully";
    }
}
