package org.example.bank_system.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    public record AssistantRequest(String message) {}
    public record AssistantReply(String answer) {}

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a helpful assistant. Keep answers short and clear.")
                .build();
    }

    @PostMapping
    public AssistantReply chat(@RequestBody AssistantRequest request) {
        if (request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message must not be empty");
        }
        try {
            String answer = chatClient.prompt()
                    .user(request.message())
                    .call()
                    .content();
            return new AssistantReply(answer);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "AI is unavailable right now, try again later");
        }
    }
}