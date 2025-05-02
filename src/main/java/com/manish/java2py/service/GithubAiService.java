package com.manish.java2py.service;


import com.manish.java2py.model.GithubAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GithubAiService {

    private final WebClient webClient;

    private final String model;

    public GithubAiService(WebClient githubWebClient, @Value("${github.model}") String model) {
        this.webClient = githubWebClient;
        this.model = model;
    }

    public Mono<String> chat(String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", 1,
                "top_p", 1,
                "messages", List.of(
                        Map.of("role", "system", "content", ""),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GithubAiResponse.class)
                .map(response -> response.getChoices().getFirst().getMessage().getContent());
    }
}
