package com.example.demo.controller;

import com.example.demo.service.taskService;
import com.example.demo.service.testTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class testTaskController {
    List<Map<String, Object>> responseList;
    @Autowired
    private testTaskService task;

    @Autowired
    private taskService details;

    @GetMapping("/getTestTasks")
    List<Map<String,Object>> getAll(){
      return task.getTaskDetails();
    }

    @GetMapping("/getTasks")
    List<Map<String,Object>> getAllTasks(){
        return details.taskDetails();
    }

    @PostMapping("/addDocument")
    public String addDocument(){
        return "Document Added";
    }

    @GetMapping("/getTaskSteps")
    List<Map<String,Object>> getSteps(){
       responseList = details.StepDetails();
       return responseList;
    }

    @PostMapping("/ChangeStatus")
    public String changeStatus(@RequestBody Map<String,String> payload){
        return details.changeStatus(payload);
    }

}
