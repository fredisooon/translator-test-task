package com.example.translatorapp.service;

import com.example.translatorapp.model.Request;
import com.example.translatorapp.utils.RequestObject;
import com.example.translatorapp.utils.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TranslateService {


    @Autowired
    private RequestService requestService;

    @Autowired
    private ExternalRequestService externalRequestService;


    public ResponseObject getTranslate(RequestObject requestObject,
                                       HttpServletRequest httpServletRequest){

        Request request = createRequestTemplate(requestObject, httpServletRequest);
        return requestService.save(request);
    }


//    public List<String> processStrings(List<String> inputStrings) {
//
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        List<String> resultStrings = Collections.synchronizedList(new ArrayList<>());
//
//        // Add all strings to the queue
//        BlockingQueue<String> queue = new LinkedBlockingQueue<>(inputStrings);
//
//        // Create 10 workers that will process strings from the queue
//        for (int i = 0; i < 10; i++) {
//            Long time1 = System.currentTimeMillis();
//            executorService.submit(() -> {
//                String stringToProcess;
//                while ((stringToProcess = queue.poll()) != null) {
//                    // Process the string
//                    String processedString = processString(stringToProcess);
//                    // Add the processed string to the result list
//                    resultStrings.add(processedString);
//                }
//            });
//            Long time2 = System.currentTimeMillis();
//            log.info("Iteration " + i + ". Execute time: " + (time2-time1));
//        }
//
//        // Wait for all threads to finish processing
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            // Handle the exception
//            e.printStackTrace();
//        }
//
//        return resultStrings;
//    }

//    private String processString(String stringToProcess) {
//        JSONObject sendObject = new JSONObject();
//        try {
//            sendObject.put("texts", stringToProcess);
//            sendObject.put("targetLanguageCode", "ru");
//            log.info(sendObject.toString());
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        String response = null;
//        try {
//            log.info("Body for request: " + sendObject.toString());
//            String badBody = "{\n" +
//                        "    \"texts\": [\"Привет\", \"Солнце\"],\n" +
//                        "    \"targetLanguageCode\": \"ja\"\n" +
//                        "}";
//            log.info(badBody);
//            response = webclient.post()
//                    .uri(new URI("https://translate.api.cloud.yandex.net/translate/v2/translate"))
//                    .header("Authorization", "Api-Key AQVN2Oq5cb-DrWy9aexake2QcasvXRcJXYjsENem")
//                    .header("Content-Type", "application/json")
//                    .body(BodyInserters.fromValue(sendObject))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .log()
//                    .block();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(response);
//            log.info(jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String resultString = null;
//        try {
//            resultString = jsonObject
//                    .getJSONArray("translations")
//                    .getJSONObject(1)
//                    .getString("text");
//            log.info(resultString);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        return resultString;
//    }


    private Request createRequestTemplate(RequestObject requestObject,
                                          HttpServletRequest httpServletRequest) {

        Request request = new Request();
        request.setREQUESTIP(httpServletRequest.getRemoteAddr());

        String dateString = httpServletRequest.getHeader("Date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(dateString, formatter);
        request.setEXECUTETIME(time);

        if (requestObject.getSource_code() != null)
            request.setSOURCECODE(requestObject.getSource_code());
        request.setTARGETCODE(requestObject.getTarget_code());


        //        List<String> stringList = Arrays.asList(requestTranslate.getText().split("\\W+"));
        List<String> stringList = Arrays.asList(requestObject.getText().split("\\P{L}+"));
        request.setInputWords(stringList);

        request.setOutputWords(externalRequestService.translateWords(request));

        return request;
    }
    public String getAvailableLanguages() {
        return externalRequestService.getAvailableLanguages();
    }
}
