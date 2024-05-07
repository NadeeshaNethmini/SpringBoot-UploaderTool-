package com.example.demo.controller;

import com.example.demo.entity.FunctionalObject;
import com.example.demo.entity.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Student> excelToStuList(InputStream inputStream) throws IOException {
        try{
            List<Student> students = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
//            System.out.println("The sheet is"+sheet);
//            System.out.println("The sheet rows"+sheet.getPhysicalNumberOfRows());

            for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                int id = (int) row.getCell(0).getNumericCellValue();
                String studentName = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                int phone = (int) row.getCell(3).getNumericCellValue();
                String phone1 = String.valueOf(phone);
//                System.out.println("The id is"+id);
//                System.out.println("The student name is"+studentName);
//                System.out.println("The email is"+email);
//                System.out.println("The phone is"+phone1);
                Student student = new Student();
                student.setId(id);
                student.setStudentName(studentName);
                student.setEmail(email);
                student.setPhone(phone1);
                students.add(student);
            }
            workbook.close();
            System.out.println("The student list is"+students);
            return students;
        }catch (Exception e){
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<FunctionalObject> excelToFunList(InputStream inputStream) {
        try{
            List<FunctionalObject> funList = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            System.out.println("The sheet is"+sheet);
            System.out.println("The sheet rows"+sheet.getPhysicalNumberOfRows());

            int rowNo=0;
            int rowCountWithData = 0;
            for (Row row : sheet) {
                boolean rowHasData = false;
                // Iterate through each cell in the row
                for (Cell cell : row) {
                    // Check if cell is not empty
                    if (cell.getCellType() != CellType.BLANK) {
                        rowHasData = true;
                        break;
                    }
                }
                // If the row has at least one non-empty cell, consider it as a row with data
                if (rowHasData) {
                    rowCountWithData++;
                }
            }
            System.out.println("Number of rows with data: " + rowCountWithData);

            for(int i = 2; i < rowCountWithData; i++) {
                Row row = sheet.getRow(i);
                FunctionalObject functionalObject = new FunctionalObject();
                functionalObject.setObjectId(row.getCell(1).getStringCellValue());
                functionalObject.setDescription(row.getCell(2).getStringCellValue());
                functionalObject.setSite(row.getCell(3).getStringCellValue());
                functionalObject.setObjLevel(row.getCell(4).getStringCellValue());
                funList.add(functionalObject);
            }
            System.out.println("The Functional Object List is"+funList);
            workbook.close();
            return funList;
        }catch (Exception e){
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static void writeToExcel(List<FunctionalObject> errors) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Errors");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ObjectId");
        header.createCell(1).setCellValue("Description");
        header.createCell(2).setCellValue("Site");
        header.createCell(3).setCellValue("ObjLevel");
        header.createCell(4).setCellValue("Error");

        int rowNum = 1;
        for (FunctionalObject error : errors) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(error.getObjectId());
            row.createCell(1).setCellValue(error.getDescription());
            row.createCell(2).setCellValue(error.getSite());
            row.createCell(3).setCellValue(error.getObjLevel());
            row.createCell(4).setCellValue(error.getError());
        }

        String filePath = "src/main/resources/static/functionalObjectErrors.xls"; // Adjust the path as needed
        Path outputPath = Paths.get(filePath);

        try {
            workbook.write(new FileOutputStream(outputPath.toFile()));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
