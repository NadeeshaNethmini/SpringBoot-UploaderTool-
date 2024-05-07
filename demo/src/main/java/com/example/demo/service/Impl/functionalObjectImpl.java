package com.example.demo.service.Impl;
import com.example.demo.entity.FunctionalObject;
import com.example.demo.repo.FunctionalObjectRepo;
import com.example.demo.service.FunctionalObjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.controller.ExcelHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class functionalObjectImpl implements FunctionalObjectService {
    String baseURL="https://ifscloud.tsunamit.com";
    StringBuilder stringBuilder=new StringBuilder(baseURL);
    String accessToken= "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJVdWtEM044dVFiMkgyOGZBNFRnWGh4b1JmMElXMUNkTXV0cjlLbDRKbmpJIn0.eyJleHAiOjE3MTUwODEwOTAsImlhdCI6MTcxNTA3NzQ5MCwiYXV0aF90aW1lIjoxNzE1MDU0NjAyLCJqdGkiOiJjMzZmOGE2YS05Yjg0LTRmNDAtYmRmMS0zYzk2ZWZlMmZjY2UiLCJpc3MiOiJodHRwczovL2lmc2Nsb3VkLnRzdW5hbWl0LmNvbS9hdXRoL3JlYWxtcy90c3V0c3QiLCJhdWQiOlsidHN1dHN0IiwiYWNjb3VudCJdLCJzdWIiOiJmMjJhOTYwNy04NzNjLTRjZWYtOGEzMi0xODE5NjdlMWRmZjUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJJRlNfYXVyZW5hIiwic2Vzc2lvbl9zdGF0ZSI6IjUxNmNhMTY0LTIxZGItNDgzNi04NzNkLTMwMmU5N2Q1NTIyZCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyIsImh0dHBzOi8vaWZzLWFwcC5naDRzdnF3NXQydXUzbDJpeXRiMWhnZXNnYi5ieC5pbnRlcm5hbC5jbG91ZGFwcC5uZXQiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtdHN1dHN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgbWljcm9wcm9maWxlLWp3dCBlbWFpbCBhdWRpZW5jZSIsInNpZCI6IjUxNmNhMTY0LTIxZGItNDgzNi04NzNkLTMwMmU5N2Q1NTIyZCIsInVwbiI6Im5hZGVlc2hhbiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZ3JvdXBzIjpbImRlZmF1bHQtcm9sZXMtdHN1dHN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJuYWRlZXNoYW4iLCJlbWFpbCI6Im5hZGVlc2hhLm5AY3JlYXRpdmVzb2Z0d2FyZS5jb20ifQ.av9_qgEzG-md9Ef7qJCdunk2vV7-4jGD8XW_t5oq1nm6Ud-WVpm6fph_6cP7rWW5wzTaQjdb03rCk6ODyhxPMWh0c_ORu2BsqK84udxAcH-qqKi-aF5rTMtWURih6Bij4KswImAyIBqilqR4aQbFV0PnbyjDkP7p-jCAdQEkjC6cG7J74S840Rhh1EHqGkzbopGoPobWgE8KfqRjvdSHs4lQxkmhg0SemSYn9mNDSyUN7f8zbf11kYYTKtdoAkqu70u-OTA4jo_61wuRR1LvNGUwCTRLVtcELzye4h4wmI2CXxTzAiEW-b7D6OSBTCE3ZzDrnvF3Bq5cn9khDrampA";
    @Autowired
    FunctionalObjectRepo functionalObject;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<FunctionalObject> save(MultipartFile file) {
        List<FunctionalObject> errorList = new ArrayList<>();
        try{
//            System.out.println("Inside the save method");
            List<FunctionalObject> funList = ExcelHelper.excelToFunList(file.getInputStream());

            //get the valid objLevel from an api call
            errorList = checkObjLevel(funList);
            System.out.println("The error list is"+errorList);

            //remove the invalid objLevel from the list
            funList.removeAll(errorList);
            System.out.println("The funList is"+funList);

            //save the valid functional objects to the database
            functionalObject.saveAll(funList);

            if(funList.size()==0){
                return errorList;
            }else{
                List<FunctionalObject> postStatus= postFunctionalObject(funList);
                System.out.println("The post status is"+postStatus);
                if(postStatus.size()>0){
                    errorList.addAll(postStatus);
                    return errorList;
                }else {
                    return errorList;
                }
            }
        }catch (Exception e) {
              return errorList;
        }
    }

    private List<FunctionalObject> postFunctionalObject(List<FunctionalObject> funList) {
//        System.out.println("Inside the postFunctionalObject method");
//        System.out.println("The functional object list is"+funList);
        HttpHeaders headers= gethttpHeaders();
        stringBuilder.setLength(0);
        String url = stringBuilder.append(baseURL).append("/main/ifsapplications/projection/v1/FunctionalObjectHandling.svc/EquipmentFunctionalSet").toString();
        System.out.println("The url is"+url);
        List<FunctionalObject> invalidList= new ArrayList<>();
        int i=0;
        for (FunctionalObject fun : funList) {
            System.out.println("Step "+i++);
            Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("Contract", fun.getSite());
            payload.put("MchCode", fun.getObjectId());
            payload.put("MchName", fun.getDescription());
            payload.put("SupContract", fun.getSite());
            payload.put("PmProgApplicationStatus", false);
            payload.put("IsCategoryObject", false);
            payload.put("IsGeographicObject", false);
            payload.put("SafetyCriticalElement", false);
            payload.put("SafeAccessCode", "NotRequired");
            payload.put("ObjLevel", fun.getObjLevel());
            payload.put("Company", "I2S100");
            HttpEntity<Map> httpEntity = new HttpEntity<>(payload, headers);
            System.out.println("The payload is"+payload);
            System.out.println("HttpEntity is"+httpEntity);
            try{
                var response= restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
                if(response.getStatusCode().toString().equals("201 CREATED")){
                    continue;
                }else {
                    fun.setError("POST API call failed");
                    invalidList.add(fun);
                }
            }catch (Exception e) {
                String errorResponse = e.getMessage();
                String errorMessage = extractErrorMessageFromJson(errorResponse);
                fun.setError(errorMessage);
                invalidList.add(fun);
            }
        }
        System.out.println("The invalid list is"+invalidList);
        System.out.println("Posted all the functional objects");
        return invalidList;
    }

    private String extractErrorMessageFromJson(String errorResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(errorResponse.substring(errorResponse.indexOf("{")));
            System.out.println("The root node is"+rootNode);
            JsonNode errorMessageNode = rootNode.path("error").path("message");
            return errorMessageNode.asText();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while parsing JSON response.";
        }
    }

    private List<FunctionalObject> checkObjLevel(List<FunctionalObject> funList) {
        HttpEntity<Void> httpEntity= new HttpEntity<>(gethttpHeaders());
        stringBuilder.setLength(0);
        stringBuilder.append(baseURL);
        String url=stringBuilder.append("/main/ifsapplications/projection/v1/FunctionalObjectHandling.svc/Reference_EquipmentObjectLevel").toString();
        try{
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
            Map<String, Object> responseMap = responseEntity.getBody();
            System.out.println("The response map is" + responseMap);
            List<Map<String, Object>> responseList = (List<Map<String, Object>>) responseMap.get("value");
            List<String> objLevelList = new ArrayList<>();
            for (Map<String, Object> map : responseList) {
                objLevelList.add((String) map.get("ObjLevel"));
            }
            List<FunctionalObject> invalidList = new ArrayList<>();
            for (FunctionalObject fun : funList) {
                if (!objLevelList.contains(fun.getObjLevel())) {
                    fun.setError("Invalid ObjLevel");
                    invalidList.add(fun);
                }
            }
            return invalidList;
        } catch (Exception e){
            List<FunctionalObject> invalidList = new ArrayList<>();
            for(FunctionalObject fun: funList){
                fun.setError("API call failed");
                invalidList.add(fun);
            }
            return invalidList;
        }
    }

    private HttpHeaders gethttpHeaders() {
        HttpHeaders headers= new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    @Override
    public List<FunctionalObject> findAll() {
        return List.of();
    }
}
