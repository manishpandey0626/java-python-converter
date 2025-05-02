package com.manish.java2py.model;

import java.util.List;

@lombok.Data
public class GithubAiResponse {
    private List<Choice> choices;

    @lombok.Data
    public static class Choice {
        private Message message;

    }

    @lombok.Data
    public static class Message {
        private String content;

    }
}
