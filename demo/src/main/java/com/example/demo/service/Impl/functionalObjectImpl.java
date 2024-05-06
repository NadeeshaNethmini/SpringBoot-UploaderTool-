package com.example.demo.service.Impl;

import com.example.demo.entity.FunctionalObject;
import com.example.demo.repo.FunctionalObjectRepo;
import com.example.demo.service.FunctionalObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.controller.ExcelHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class functionalObjectImpl implements FunctionalObjectService {
    String baseURL="https://ifscloud.tsunamit.com";
    StringBuilder stringBuilder=new StringBuilder(baseURL);
    String accessToken= "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJVdWtEM044dVFiMkgyOGZBNFRnWGh4b1JmMElXMUNkTXV0cjlLbDRKbmpJIn0.eyJleHAiOjE3MTQ5ODU5NTgsImlhdCI6MTcxNDk4MjM1OCwiYXV0aF90aW1lIjoxNzE0OTc0NjMzLCJqdGkiOiIzMmYwMWMzYy05YmRhLTRiNDAtOTNmOS01YTA5MWFlOWE1ZDMiLCJpc3MiOiJodHRwczovL2lmc2Nsb3VkLnRzdW5hbWl0LmNvbS9hdXRoL3JlYWxtcy90c3V0c3QiLCJhdWQiOlsidHN1dHN0IiwiYWNjb3VudCJdLCJzdWIiOiJmMjJhOTYwNy04NzNjLTRjZWYtOGEzMi0xODE5NjdlMWRmZjUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJJRlNfYXVyZW5hIiwic2Vzc2lvbl9zdGF0ZSI6IjZkYmM1ODJkLTBjNjUtNDQ4Yi1hZTY1LWYzYWEwNGIxYzFlMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyIsImh0dHBzOi8vaWZzLWFwcC5naDRzdnF3NXQydXUzbDJpeXRiMWhnZXNnYi5ieC5pbnRlcm5hbC5jbG91ZGFwcC5uZXQiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtdHN1dHN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgbWljcm9wcm9maWxlLWp3dCBlbWFpbCBhdWRpZW5jZSIsInNpZCI6IjZkYmM1ODJkLTBjNjUtNDQ4Yi1hZTY1LWYzYWEwNGIxYzFlMSIsInVwbiI6Im5hZGVlc2hhbiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZ3JvdXBzIjpbImRlZmF1bHQtcm9sZXMtdHN1dHN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJuYWRlZXNoYW4iLCJlbWFpbCI6Im5hZGVlc2hhLm5AY3JlYXRpdmVzb2Z0d2FyZS5jb20ifQ.I2dKlHh1awbUaarWBmhZW4ZxmpEV1cyspY0schO_tCzhRfZhK33yHWD-npql-gOqaSPlL3hg_i1KCzmcYzeKDORwrYeu2cb49IR7V6EfmfRAcZ53eCtpXLbgEf9JAtKrl4LDDyrFdUFtvstJ3ZQm5-WD-rHViMcMZXSuOtttE7InHETkYJtSKCz2JHRfavV_OFFqfeOCpup1uaLBItwvj9IM9NCkUUjuAiQx3eltUbjveHypvdhV4l-u0mXi6W2yUx_Zq70JuTmD9YD4nEgR2g9m9uKYuO8_Zl_UFeNZxOKibua95Y_NmJhhh7sat4sJsqD_MU4sULR5vQjm5BH4qg";
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
//            System.out.println("The error list is"+errorList);

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
            var response= restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            System.out.println("The response is"+response);
            System.out.println("The response status is"+response.getStatusCode());
            System.out.println((response.getStatusCode().toString().equals("201 CREATED")));
            if(response.getStatusCode().toString().equals("201 CREATED")){
                continue;
            }else {
                fun.setError("Could not post the functional object");
                invalidList.add(fun);
            }
        }
        System.out.println("Posted all the functional objects");
        return invalidList;
    }

    private List<FunctionalObject> checkObjLevel(List<FunctionalObject> funList) {
        HttpEntity<Void> httpEntity= new HttpEntity<>(gethttpHeaders());
        stringBuilder.setLength(0);
        stringBuilder.append(baseURL);
        String url=stringBuilder.append("/main/ifsapplications/projection/v1/FunctionalObjectHandling.svc/Reference_EquipmentObjectLevel").toString();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
        Map<String, Object> responseMap= responseEntity.getBody();
        List<Map<String, Object>> responseList= (List<Map<String, Object>>) responseMap.get("value");
        List<String> objLevelList= new ArrayList<>();
        for (Map<String, Object> map: responseList){
            objLevelList.add((String) map.get("ObjLevel"));
        }
        List<FunctionalObject> invalidList= new ArrayList<>();
        for (FunctionalObject fun: funList){
            if(!objLevelList.contains(fun.getObjLevel())){
                fun.setError("Invalid ObjLevel");
                invalidList.add(fun);
            }
        }
//        System.out.println("The invalid objLevel are"+invalidList);
        return invalidList;
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
