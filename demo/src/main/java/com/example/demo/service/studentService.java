package com.example.demo.service;

import com.example.demo.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface studentService {
    void save(MultipartFile file);
    List<Student> findAll();
}
