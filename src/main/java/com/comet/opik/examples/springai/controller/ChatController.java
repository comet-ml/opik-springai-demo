package com.comet.opik.examples.springai.controller;

import com.comet.opik.examples.springai.dto.ChatRequest;
import com.comet.opik.examples.springai.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller that provides endpoints for interacting with chat-related operations.
 * Handles HTTP requests for asking questions and processes the responses from the ChatService.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ask-me")
    public String askMe(@RequestParam(defaultValue = "Tell me a joke", name = "question") String question) {
        return chatService.askQuestion(question);
    }

    @PostMapping("/ask")
    public String ask(@RequestBody String question) {
        return chatService.askQuestion(question);
    }

    @PostMapping("/ask-enhanced")
    public String ask(@RequestBody ChatRequest request) {
        return chatService.askQuestion(request.question(), request.tags(), request.metadata());
    }

    @PostMapping("/ask-with-params")
    public String ask(
            @RequestParam String question,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Map<String, String> metadata) {
        return chatService.askQuestion(question, tags, metadata);
    }
}