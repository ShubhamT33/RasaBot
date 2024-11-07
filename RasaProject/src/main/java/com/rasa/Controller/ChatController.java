package com.rasa.Controller;

import com.rasa.Service.RasaChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private RasaChatbotService rasaChatbotService;

    @PostMapping
    public String chatWithBot(@RequestBody String message) {
        return rasaChatbotService.sendMessageToRasa(message);
    }
}
