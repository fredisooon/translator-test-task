package com.example.translatorapp.service;

import com.example.translatorapp.model.Request;
import com.example.translatorapp.utils.RequestObject;
import com.example.translatorapp.utils.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TranslateService {

    private final RequestService requestService;

    private final ExternalRequestService externalRequestService;

    public TranslateService(RequestService requestService, ExternalRequestService externalRequestService) {
        this.requestService = requestService;
        this.externalRequestService = externalRequestService;
    }


    public ResponseObject getTranslate(RequestObject requestObject,
                                       HttpServletRequest httpServletRequest){

        Request request = createRequestTemplate(requestObject, httpServletRequest);
        return requestService.save(request);
    }

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
        long start = System.currentTimeMillis();
        request.setOutputWords(externalRequestService.translateWords(request));
        long stop = System.currentTimeMillis();
        log.info("Execute time " + (stop - start));
        return request;
    }
    public String getAvailableLanguages() {
        return externalRequestService.getAvailableLanguages();
    }
}
