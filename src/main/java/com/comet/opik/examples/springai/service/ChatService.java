package com.comet.opik.examples.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling chat-related operations.
 * Provides functionality to interact with an underlying LLM chat client.
 */
@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String askQuestion(@NonNull String question) {
        return chatClient
                .prompt(new Prompt(question))
                .call()
                .content();
    }
}