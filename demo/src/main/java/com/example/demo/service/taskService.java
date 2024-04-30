package com.example.demo.service;
import java.util.List;
import java.util.Map;

public interface taskService {
    List<Map<String, Object>> taskDetails();

    List<Map<String,Object>> StepDetails();

    String changeStatus(Map<String,String> payload);

}
