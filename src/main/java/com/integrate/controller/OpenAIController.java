package com.integrate.controller;

import com.integrate.service.openAi.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/openai/api")
public class OpenAIController {

    private final OpenAiService openAiService;

    public OpenAIController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = openAiService.askChatGpt(userMessage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateJsonLogic")
    public String generateJsonLogic(@RequestBody Map<String, String> request)  throws Exception{
        try {
            // for proper respopnse format need to implement the common Response class with :- status,code,message & object
            String userMessage = request.get("userMessage");
            return openAiService.generateJsonLogic(userMessage);
        } catch (IOException e) {
            e.printStackTrace();  // later We will use a log.error()
            return "Error occurred while generating JSON Logic";
        }
    }
}
