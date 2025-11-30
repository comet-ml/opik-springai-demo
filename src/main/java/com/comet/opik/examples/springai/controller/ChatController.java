package com.comet.opik.examples.springai.controller;

import com.comet.opik.examples.springai.service.ChatService;
import org.springframework.web.bind.annotation.*;

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
}