package com.example.translatorapp.service;

import com.example.translatorapp.config.WebClientConfig;
import com.example.translatorapp.model.Request;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;


@Slf4j
@Service
public class ExternalRequestService {

    @Autowired
    private static WebClient webClient = WebClientConfig.getWebClient();

    private final Environment env;
    public ExternalRequestService(Environment env) {
        this.env = env;
    }
    private static String SOURCE_CODE;
    private static String TARGET_CODE;
    private final int THREAD_COUNT = 10;


    public List<String> translateWords(Request request) {
        if (request.getSOURCECODE() != null)
            SOURCE_CODE = request.getSOURCECODE();
        TARGET_CODE = request.getTARGETCODE();

        if (request.getInputWords() != null) {
            return translateWord(request.getInputWords());
//            return translateWordMultithreading(request.getInputWords());
        }
        else
            return new ArrayList<>();
    }

    private List<String> translateWord(List<String> inputList) {
        List<String> resultList = new ArrayList<>();
        for (String word : inputList) {
            try {
                resultList.add(getExternalRequest(word));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    private synchronized String getExternalRequest(String word) throws  URISyntaxException {

        URI uri = new URI(Objects.requireNonNull(env.getProperty("yandex.url.translate")));
        String key = "Api-Key " + env.getProperty("API_KEY");
        String response = webClient.post()
                .uri(uri)
                .header("Authorization", key)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(formRequestBody(word)))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonObject responseData = JsonParser.parseString(Objects.requireNonNull(response)).getAsJsonObject();

        String string = responseData
                .getAsJsonArray("translations")
                .get(0)
                .getAsJsonObject()
                .get("text")
                .getAsString();
        return string;
    }

    private String formRequestBody(String word) {
        JsonObject requestData = new JsonObject();

        requestData.addProperty("texts", word);
        requestData.addProperty("targetLanguageCode", TARGET_CODE);

        if (SOURCE_CODE != null)
            requestData.addProperty("sourceLanguageCode", SOURCE_CODE);

        return requestData.toString();
    }

    public String  getAvailableLanguages() {

        URI uri;
        try {
            uri = new URI(Objects.requireNonNull(env.getProperty("yandex.url.listLanguages")));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String key = "Api-Key " + env.getProperty("API_KEY");

        return webClient.post()
                .uri(uri)
                .header("Authorization", key)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private List<String> translateWordMultithreading(List<String> inputStrings) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        List<String> resultStrings = Collections.synchronizedList(new ArrayList<>());

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(inputStrings);


        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                String stringToProcess;
                while ((stringToProcess = queue.poll()) != null) {
                    String processedString = processString(stringToProcess);
                    resultStrings.add(processedString);
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resultStrings;
    }

    private String processString(String stringToProcess) {
        String string;
        try {
            string = getExternalRequest(stringToProcess);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return string;
    }



}
