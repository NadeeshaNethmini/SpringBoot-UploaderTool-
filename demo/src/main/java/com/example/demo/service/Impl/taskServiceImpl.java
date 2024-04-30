package com.example.demo.service.Impl;

import com.example.demo.service.taskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class taskServiceImpl implements taskService {
    String token="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJVdWtEM044dVFiMkgyOGZBNFRnWGh4b1JmMElXMUNkTXV0cjlLbDRKbmpJIn0.eyJleHAiOjE3MTQ0NzI5NjEsImlhdCI6MTcxNDQ2OTM2MSwiYXV0aF90aW1lIjoxNzE0NDU2NTAyLCJqdGkiOiI5ZDdjZDU2NS02MzkzLTRjYmUtOGFhNC01OGQ1MTEzZWMxZTAiLCJpc3MiOiJodHRwczovL2lmc2Nsb3VkLnRzdW5hbWl0LmNvbS9hdXRoL3JlYWxtcy90c3V0c3QiLCJhdWQiOlsidHN1dHN0IiwiYWNjb3VudCJdLCJzdWIiOiJmMjJhOTYwNy04NzNjLTRjZWYtOGEzMi0xODE5NjdlMWRmZjUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJJRlNfYXVyZW5hIiwic2Vzc2lvbl9zdGF0ZSI6ImNhYTA3NWE0LThlODEtNDk0Zi1iYjgzLWQ2MGMxNmJkODk5MyIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyIsImh0dHBzOi8vaWZzLWFwcC5naDRzdnF3NXQydXUzbDJpeXRiMWhnZXNnYi5ieC5pbnRlcm5hbC5jbG91ZGFwcC5uZXQiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtdHN1dHN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgbWljcm9wcm9maWxlLWp3dCBlbWFpbCBhdWRpZW5jZSIsInNpZCI6ImNhYTA3NWE0LThlODEtNDk0Zi1iYjgzLWQ2MGMxNmJkODk5MyIsInVwbiI6Im5hZGVlc2hhbiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZ3JvdXBzIjpbImRlZmF1bHQtcm9sZXMtdHN1dHN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJuYWRlZXNoYW4iLCJlbWFpbCI6Im5hZGVlc2hhLm5AY3JlYXRpdmVzb2Z0d2FyZS5jb20ifQ.FOtoMWukvVcnecaXT2i9E-od4a59R-C2l1sqpz_3PH5rJ1gaOmqbFZ-c2RNo8fRJm3g5XuDNOh61jl_xKTZuSeOrNG2k9S3_wtRgX-OAZ9z5oaFRTLGttYM_RUF3jsKKY9bp4fkQF-BYBerfudxGtpqYLgHi98XJchRbIh3q3YmNtoEotO95inyNd0cEfTNlWi_sdBSfZmv19v3jzCUVH2MNiBrd3-combwBic853J3ZhFopPnvEel9fCipkZZ4zfe6a8j13he5c4qDRtZ6Y-SN7yayIGn6jeWugW7qEMIEjv5h8NV-ke_NU5WN7IWk0sqQFW8bJM_U7HPcuoH1PBw";
    String baseURL="https://ifscloud.tsunamit.com";
    StringBuilder stringBuilder=new StringBuilder(baseURL);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Map<String, Object>> taskDetails() {
        HttpEntity<Void> httpEntity= new HttpEntity<>(gethttpHeaders());
        String url= stringBuilder.append("/main/ifsapplications/projection/v1/WorkTasksHandling.svc/JtTaskSet(TaskSeq=1)").toString();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
//        System.out.println(responseEntity.getBody());
        Map<String, Object> responseMap= responseEntity.getBody();
        List<Map<String, Object>> responseList= new ArrayList<>();
        for (Map.Entry<String, Object> entry: responseMap.entrySet()){
            Map<String, Object> map= new HashMap<>();
            map.put(entry.getKey(), entry.getValue());
            responseList.add(map);
        }
        System.out.println(responseList);
        return responseList;
    }

    @Override
    public List<Map<String, Object>> StepDetails() {
        HttpEntity<Void> httpEntity= new HttpEntity<>(gethttpHeaders());
        String url= stringBuilder.append("/main/ifsapplications/projection/v1/WorkTaskHandling.svc/JtTaskSet(TaskSeq=1)/WorkTaskStepsArray").toString();
//        System.out.println("The url is "+url);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
//        System.out.println(responseEntity.getBody());
        Map<String, Object> responseMap= responseEntity.getBody();
        //take the value part of the responseMap
        List<Map<String, Object>> responseList= new ArrayList<>();
        for (Map.Entry<String, Object> entry: responseMap.entrySet()){
            Map<String, Object> map= new HashMap<>();
            if(entry.getKey().equals("value")){
                List<Map<String, Object>> valueList= (List<Map<String, Object>>) entry.getValue();
                for (Map<String, Object> valueMap: valueList){
                    responseList.add(valueMap);
                }
            }
        }
        return responseList;
    }

    @Override
    public String changeStatus(Map<String,String> payload) {
        try {
            String etag = "W/\"Vy8iQUFBWWlPQUFNQUFBMEFXQUFNOjMi\"";

            var taskSeq = 2;

            // Set the headers
            HttpHeaders headers = gethttpHeadersWithEtag(etag);

            // Create the HttpEntity with headers and body
            HttpEntity<Map> httpEntity = new HttpEntity<>(payload, headers);

            System.out.println("The httpEntity is "+httpEntity);

            String apiEndPoint =
                    "/main/ifsapplications/projection/v1/WorkTaskHandling.svc/JtTaskSet(TaskSeq=" + taskSeq + ")/WorkTaskStepsArray(TaskSeq=" + taskSeq + ",TaskStepSeq=13)/IfsApp.WorkTaskHandling.JtTaskStep_Complete";

            stringBuilder.setLength(0);
            String url = stringBuilder.append(baseURL).append(apiEndPoint).toString();
            System.out.println("The url is " + url);
            var response= restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

            //send the post request
            return "Status Changed";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }

    private HttpHeaders gethttpHeadersWithEtag(String etag) {
        HttpHeaders headers= new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken= token;
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("If-Match", etag.toString());
        System.out.println("The headers are "+headers);
        return headers;
    }

    private HttpHeaders gethttpHeaders() {
        HttpHeaders headers= new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken= token;
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
