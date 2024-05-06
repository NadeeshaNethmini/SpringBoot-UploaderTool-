package com.example.demo.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.Id;

@Data
@Entity
public class FunctionalObject {
    @Id
    String ObjectId;
    String Description;
    String Site;
    String ObjLevel;
    String error;
}
