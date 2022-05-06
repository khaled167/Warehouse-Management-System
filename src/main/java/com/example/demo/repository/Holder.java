package com.example.demo.repository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Holder {
private long id;
private String editType,newValue;
}
