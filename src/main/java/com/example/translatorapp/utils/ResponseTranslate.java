package com.example.translatorapp.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTranslate {

    private String translated_text;
    private String source_language;
    private String target_language;
}
