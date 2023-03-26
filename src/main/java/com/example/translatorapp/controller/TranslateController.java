package com.example.translatorapp.controller;


import com.example.translatorapp.service.TranslateService;
import com.example.translatorapp.utils.RequestObject;
import com.example.translatorapp.utils.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/translate")
public class TranslateController {

    private final TranslateService translateService;

    public TranslateController(TranslateService translateService) {
        this.translateService = translateService;
    }


    @PostMapping()
    public ResponseObject translate(@RequestBody RequestObject body,
                                    HttpServletRequest request){
        return translateService.getTranslate(body, request);
    }


    @GetMapping("/languages")
    public String availableLanguages() {
        return translateService.getAvailableLanguages();
    }

}
