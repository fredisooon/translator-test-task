package com.example.translatorapp.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData {
    private Long ID;
    private String SOURCE;
    private Long REQUESTID;
}
