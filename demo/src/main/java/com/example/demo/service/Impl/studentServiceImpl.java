package com.example.demo.service.Impl;

import com.example.demo.entity.Student;
import com.example.demo.repo.StudentRepo;
import com.example.demo.service.studentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.controller.ExcelHelper;

import java.io.IOException;
import java.util.List;

@Service
public class studentServiceImpl implements studentService {
    @Autowired
    StudentRepo studentRepo;

    @Override
    public void save(MultipartFile file){
        try {
            List<Student> stuList = ExcelHelper.excelToStuList(file.getInputStream());
            System.out.println("The Student List is"+stuList);
            studentRepo.saveAll(stuList);
        }catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    @Override
    public List<Student> findAll(){
        return studentRepo.findAll();
    }

}
