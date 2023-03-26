package com.example.translatorapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
class StringTask implements Runnable {
    private final List<String> strings;
    private final List<String> result;
    private final WebClient webClient = WebClient.create();


    public StringTask(List<String> strings, List<String> result) {
        this.strings = strings;
        this.result = result;
    }

    @Override
    public void run() {
        for (String string : strings) {
            // Process the string
            String processedString = null;
            try {
                processedString = processString(string);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            // Add the processed string to the result list
            synchronized (result) {
                result.add(processedString);
            }
        }
    }

    private String processString(String string) throws URISyntaxException {


//        String resultString = webClient.post()
//                .uri(new URI("https://translate.api.cloud.yandex.net/translate/v2/translate"))
//                .header("Authorization", "Api-Key AQVN2Oq5cb-DrWy9aexake2QcasvXRcJXYjsENem")
//                .header("Content-Type", "application/json")
//                .body(BodyInserters.fromValue("{\n" +
//                        "    \"texts\": [\"Привет\", \"Солнце\"],\n" +
//                        "    \"targetLanguageCode\": \"ja\"\n" +
//                        "}"))
//                .retrieve()
//                .bodyToMono(String.class)
//                .log()
//                .block();

        return "asd";
    }
}
