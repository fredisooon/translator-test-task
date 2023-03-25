package com.example.translatorapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputData {
    private Long ID;
    private String SOURCE;
    private Long REQUESTID;
}
