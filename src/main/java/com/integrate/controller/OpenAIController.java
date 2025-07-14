package com.integrate.controller;

import com.integrate.openAI.OpenAIService;
import com.integrate.response.ApiResponse;
import com.integrate.service.openAi.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/openai/api")
public class OpenAIController {

    private final OpenAiService openAiService;
    @Autowired
    OpenAIService openAIService;

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
    public ResponseEntity generateJsonLogic(@RequestBody Map<String, String> request)  throws Exception{
        try {
            // for proper respopnse format need to implement the common Response class with :- status,code,message & object
            String userMessage = request.get("userMessage");
            return openAiService.generateJsonForTheGivenDescription(userMessage);
        } catch (IOException e) {
            e.printStackTrace();  // later We will use a log.error()
            return ResponseEntity.ok(new ApiResponse("ERROR", 500,e.getMessage()));
        }
    }


    @PostMapping("/fetchJsonLogicViaOpenAI")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity generateJsonForTheGivenDescription(@RequestBody Map<String, String> request) throws Exception {
        try {
            String ticketId = request.get("ticketId");
            String prompt = request.get("prompt");
            return openAIService.fetchJsonLogicViaOpenAI(ticketId, prompt);
        } catch (Exception e) {
            e.printStackTrace();  // later We will use a log.error()
            return ResponseEntity.ok(new ApiResponse("ERROR", 500, e.getMessage()));
        }
    }
}
