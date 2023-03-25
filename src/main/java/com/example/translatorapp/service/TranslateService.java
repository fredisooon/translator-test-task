package com.example.translatorapp.service;

import com.example.translatorapp.model.Request;
import com.example.translatorapp.utils.RequestTranslate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TranslateService {


    @Autowired
    private RequestService requestService;

    public String getTranslate(RequestTranslate requestTranslate,
                               HttpServletRequest httpServletRequest) throws URISyntaxException {


        log.info(httpServletRequest.getRemoteAddr());
        String dateString = httpServletRequest.getHeader("Date");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(dateString, formatter);

        log.info(requestTranslate.getText());
//        List<String> stringList = Arrays.asList(requestTranslate.getText().split("\\W+"));
        List<String> stringList = Arrays.asList(requestTranslate.getText().split("\\P{L}+"));

        for (String str: stringList) {
            log.info(str);
        }

        Request request = new Request();
        request.setREQUESTIP(httpServletRequest.getRemoteAddr());
        request.setEXECUTETIME(time);
        request.setTRANSLATEPARAM(requestTranslate.getTranslate_code());
        request.setInputWords(stringList);

        List<String> listCopy = new ArrayList<>(stringList);
        listCopy.replaceAll(string -> {
            StringBuilder stringBuilder = new StringBuilder(string);
            stringBuilder.reverse();
            return stringBuilder.toString();
        });
        request.setOutputWords(listCopy);


        requestService.save(request);

        WebClient client = WebClient.builder().build();



        log.info("OK");
//        return "OK";
        return client.post()
                .uri(new URI("https://translate.api.cloud.yandex.net/translate/v2/translate"))
                .header("Authorization", "Api-Key AQVN2Oq5cb-DrWy9aexake2QcasvXRcJXYjsENem")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\n" +
                        "    \"texts\": [\"Привет\", \"Солнце\"],\n" +
                        "    \"targetLanguageCode\": \"ja\"\n" +
                        "}"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<String> getAvailableLanguages() {
        return Arrays.asList("ru", "en", "uk", "be", "ch", "ja");
    }
}
