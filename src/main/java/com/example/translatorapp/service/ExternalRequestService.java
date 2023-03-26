package com.example.translatorapp.service;


import com.example.translatorapp.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ExternalRequestService {

    private static String SOURCE_CODE;
    private static String TARGET_CODE;
    private final WebClient webClient;

    public ExternalRequestService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    private Environment env;

    public List<String> translateWords(Request request) {
        if (request.getSOURCECODE() != null)
            SOURCE_CODE = request.getSOURCECODE();
        TARGET_CODE = request.getTARGETCODE();

        if (request.getInputWords() != null)
            return translateWord(request.getInputWords());
        else
            return new ArrayList<>();
    }

    private List<String> translateWord(List<String> inputList) {
        List<String> resultList = new ArrayList<>();
        for (String word : inputList) {
            try {
                resultList.add(getExternalRequest(word));
            } catch (JSONException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    private String getExternalRequest(String word) throws JSONException, URISyntaxException {
        URI uri = new URI(env.getProperty("yandex.url.translate"));
        String key = "Api-Key " + env.getProperty("API_KEY");
        String response = webClient.post()
                .uri(uri)
                .header("Authorization", key)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(formRequestBody(word)))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info(response);
        JSONObject responseData = new JSONObject(response);
        String str = responseData.getJSONArray("translations").getJSONObject(0).getString("text");
        log.info(str);

        return str;
    }

    private String formRequestBody(String word) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("texts", word);
            requestData.put("targetLanguageCode", TARGET_CODE);

            if (SOURCE_CODE != null)
                requestData.put("sourceLanguageCode", SOURCE_CODE);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        log.info("Request payload for word " + word + ": " + requestData);
        return requestData.toString();
    }

    public String  getAvailableLanguages() {
        URI uri = null;
        try {
            uri = new URI(env.getProperty("yandex.url.listLanguages"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String key = "Api-Key " + env.getProperty("API_KEY");
        String response = webClient.post()
                .uri(uri)
                .header("Authorization", key)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
