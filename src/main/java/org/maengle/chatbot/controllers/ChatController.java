package org.maengle.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.chatbot.entities.ChatData;
import org.maengle.chatbot.services.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * /chat?message=문장...
     * @param message
     * @return
     */
    @GetMapping
    public ChatData chat(@RequestParam("message") String message) {
        return chatService.chatProcess(message);
    }
}
