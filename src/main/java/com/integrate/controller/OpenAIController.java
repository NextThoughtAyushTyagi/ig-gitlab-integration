package com.integrate.controller;

import com.integrate.service.openai.OpenAIEmbeddingService;
import com.integrate.service.openai.OpenAIService;
import com.integrate.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/openai/api")
@CrossOrigin(origins = "http://localhost:3000")
public class OpenAIController {

    @Autowired
    OpenAIService openAIService;

    @Autowired
    OpenAIEmbeddingService openAIEmbeddingService;

    @PostMapping("/fetchJsonLogicViaOpenAI")
    public ResponseEntity generateJsonForTheGivenDescription(@RequestBody Map<String, String> request) throws Exception {
        try {
            String ticketId = request.get("ticketId");
            String prompt = request.get("prompt");
//            openAIEmbeddingService.vectorTest();
            return openAIService.fetchJsonLogicViaOpenAI(ticketId, prompt);

        } catch (Exception e) {
            e.printStackTrace();  // later We will use a log.error()
            return ResponseEntity.ok(new ApiResponse("ERROR", 500, e.getMessage()));
        }
    }
}
