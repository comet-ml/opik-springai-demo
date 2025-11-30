package com.comet.opik.examples.springai.service;

import com.comet.opik.examples.springai.helpers.OpikTracingUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service class responsible for handling chat-related operations.
 * Provides functionality to interact with an underlying LLM chat client.
 */
@Service
public class ChatService {

    private final Tracer tracer;
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder, Tracer tracer) {
        this.chatClient = chatClientBuilder.build();
        this.tracer = tracer;
    }

    public String askQuestion(@NonNull String question) {
        return chatClient
                .prompt(new Prompt(question))
                .call()
                .content();
    }

    public String askQuestion(@NonNull String question, List<String> tags, Map<String, String> metadata) {

        Span span = tracer.currentSpan();
        if (Objects.nonNull(span)) {
            OpikTracingUtils.setTags(span, tags);
            OpikTracingUtils.setMetadata(span, metadata);
        }

        return chatClient
                .prompt(new Prompt(question))
                .call()
                .content();
    }
}