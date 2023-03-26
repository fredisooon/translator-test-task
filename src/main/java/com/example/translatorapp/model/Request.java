package com.example.translatorapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private Long ID;
    private String REQUESTIP;
    private LocalTime EXECUTETIME;
    private String SOURCECODE;
    private String TARGETCODE;

    @Nullable
    private List<String> inputWords;

    @Nullable
    private List<String> outputWords;

}
