package com.example.translatorapp.service;


import com.example.translatorapp.utils.RequestTranslate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TranslateService {


    public String getTranslate(RequestTranslate requestTranslate,
                                             HttpServletRequest request) throws URISyntaxException {
        log.info(request.getRemoteAddr());

        WebClient client = WebClient.builder().build();


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
