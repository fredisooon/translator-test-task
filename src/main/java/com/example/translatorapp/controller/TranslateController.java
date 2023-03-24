package com.example.translatorapp.controller;


import com.example.translatorapp.service.TranslateService;
import com.example.translatorapp.utils.RequestTranslate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
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
    public String translate(@RequestBody RequestTranslate body,
                                          HttpServletRequest request) throws URISyntaxException, IOException, InterruptedException {
        return translateService.getTranslate(body, request);
    }


    @GetMapping("/languages")
    public List<String> availableLanguages() {
        return translateService.getAvailableLanguages();
    }

}
