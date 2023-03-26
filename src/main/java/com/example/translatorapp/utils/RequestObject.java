package com.example.translatorapp.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestObject {

    private String text;
    @Nullable
    private String source_code;
    private String target_code;
}
