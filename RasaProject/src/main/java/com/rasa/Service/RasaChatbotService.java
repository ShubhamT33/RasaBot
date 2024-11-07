//package com.rasa.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.client.HttpClientErrorException;
//
//@Service
//public class RasaChatbotService {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private static final String RASA_URL = "http://localhost:5005/webhooks/rest/webhook";
//
//    public String sendMessageToRasa(String message) {
//        try {
//            // Create the payload with just the message field (no sender)
//            String payload = String.format("{\"message\":\"%s\"}", message);
//
//            // Create the headers (no authorization token required)
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Content-Type", "application/json");
//
//            // Create the HttpEntity with payload and headers
//            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
//
//            // Make the request to Rasa server
//            ResponseEntity<String> response = restTemplate.exchange(RASA_URL, HttpMethod.POST, entity, String.class);
//
//            // Return the response from Rasa
//            return response.getBody();
//        } catch (HttpClientErrorException e) {
//            return "Error communicating with Rasa: " + e.getMessage();
//        }
//    }
//}
package com.rasa.Service;

import com.rasa.Entity.ChatMessage;
import com.rasa.Repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class RasaChatbotService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static final String RASA_URL = "http://localhost:5005/webhooks/rest/webhook";

    public String sendMessageToRasa(String message) {
        try {
            String payload = String.format("{\"message\":\"%s\"}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.exchange(RASA_URL, HttpMethod.POST, entity, String.class);

            // Save the message and response to MongoDB
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserMessage(message);
            chatMessage.setBotResponse(response.getBody());
            chatMessage.setTimestamp(LocalDateTime.now());
            chatMessageRepository.save(chatMessage);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            return "Error communicating with Rasa: " + e.getMessage();
        }
    }
}
