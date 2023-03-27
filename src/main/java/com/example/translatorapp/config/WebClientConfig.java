package com.example.translatorapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;



@Configuration
public class WebClientConfig {

    @Bean
    private static HttpClient getHttpClient() {
        return HttpClient.create();
    }
    @Bean
    public static WebClient getWebClient()  {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .build();
    }

}