package com.example.demo.service.Impl;

import com.example.demo.service.testTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class testTaskServiceImpl implements testTaskService {

    String baseUrl= "https://jsonplaceholder.typicode.com";

    StringBuilder stringBuilder= new StringBuilder(baseUrl);

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<Map<String, Object>> getTaskDetails() {
        HttpEntity <Void> httpEntity= new HttpEntity<>(gethttpHeaders());
        String url=stringBuilder.append("/posts").toString();
        var response=restTemplate.exchange(url, HttpMethod.GET,httpEntity,List.class);
        return response.getBody();
    }


    private HttpHeaders gethttpHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
