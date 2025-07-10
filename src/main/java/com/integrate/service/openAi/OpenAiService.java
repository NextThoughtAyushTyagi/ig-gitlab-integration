package com.integrate.service.openAi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrate.pojo.open.ai.OpenAiRequest;
import com.integrate.response.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Service
public class OpenAiService {

    private final WebClient webClient;

    // later move into ConfigUtil

    @Value("${spring.prism.api.key}")
    String prismApiKey;

    @Value("${spring.prism.api.base.url}")
    String prismApiBaseUrl;

    @Value("${spring.chat.gpt.model}")
    String chatGptModel;

    @Value("${spring.chat.gpt.system.role}")
    String chatGptSystemRole;

    @Value("${spring.chat.gpt.end.user.role}")
    String getChatGptEndUserrole;

    public OpenAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public String askChatGpt(String userMessage) {
        Map<String, Object> message = Map.of(
                "model", "gpt-4", // or "gpt-3.5-turbo"
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                )
        );

        return this.webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "OPENAI_API_KEY")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    return (String) messageObj.get("content");
                })
                .block();
    }
    public ResponseEntity generateJsonForTheGivenDescription(String userMessage) throws Exception {
        String generatedJsonLogic = null;
        try {
            OpenAiRequest.Message message1 = new OpenAiRequest.Message();
            message1.setRole(chatGptSystemRole);
            //message1.setContent("You are an assistant that generates JSON logic based on user input. Respond with exact and valid JSON without any explanations, formatting, or additional characters such as newlines or spaces. Use the operator directly, as JSON logic requires.");
            // message1.setContent("You are an assistant that generates JSON logic based on user input.Respond with exact and valid JSON Logic without any explanations, formatting, or additional characters such as newlines or spaces,also without redundant keys/statements and follows jamsesso/json-logic-java & js-logic npm 2.0.2 package structures  & use logical operators such as ==, !=, >=, <=, and, or, and in.Use the operator directly, as JSON logic requires.");
            message1.setContent("You are an assistant that generate compact and standard JSON Logic and do not use 'then' in the JSON Logic.Also do n't use the key json inside content.Respond with exact and valid JSON Logic format without any explanations, formatting, or additional characters such as newlines or spaces. Use the operator directly, as JSON logic requires with proper java code like swicth case,if else(for else part don't use key name in the key value pair). Ensure the JSON Logic output uses compact logic without redundant keys/statements and follows jamsesso json-logic-java & js-logic npm 2.0.2 package structures accurately.");
            // message1.setContent("You are an assistant that generate compact and standard JSON Logic and do not use 'then' in the JSON Logic. Respond with exact and valid JSON Logic without any explanations, formatting, or additional characters such as newlines or spaces. Use the operator directly, as JSON logic requires with proper JSON structure without any redundant keys/statements and follows jamsesso/json-logic-java & js-logic npm 2.0.2 package structures accurately.");
            OpenAiRequest.Message message2 = new OpenAiRequest.Message();
            message2.setRole(getChatGptEndUserrole);
            message2.setContent(userMessage);
            // Create the GPTRequest object
            OpenAiRequest request = new OpenAiRequest();
            request.setModel(chatGptModel);
            request.setMessages(Arrays.asList(message1, message2));
            //Convert the Entity object into jsonString
            ObjectMapper requestObjectMapper = new ObjectMapper();
            String jsonStringRequest = requestObjectMapper.writeValueAsString(request);
            System.out.println("jsonPayload jsonPayload jsonPayload :::::"+jsonStringRequest);
            String jsonStringResponse = callOpenAiApi(jsonStringRequest);
            String ERROR_CASE= "ERROR";
            // fetching the generated jsonLogic
            if(jsonStringResponse!=null) {
                if (jsonStringResponse.equals(ERROR_CASE)) {
                    return ResponseEntity.ok(new ApiResponse("ERROR", 500, "Failed to fetch the Json Logic"));
                } else {
                    ObjectMapper responseObjectMapper = new ObjectMapper();
                    try {
                        // Parse the JSON string into a JsonNode
                        JsonNode rootNode = responseObjectMapper.readTree(jsonStringResponse);
                        JsonNode choicesNode = rootNode.path("choices");
                        if (choicesNode.isArray() && choicesNode.size() > 0) {
                            JsonNode messageNode = choicesNode.get(0).path("message");
                            generatedJsonLogic = messageNode.path("content").asText();
                            // generated json Logic
                            System.out.println("generatedJsonLogic: " + generatedJsonLogic);
                        } else {
                            System.out.println("No choices found.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.ok(new ApiResponse("ERROR", 500, e.getMessage()));
                    }
                    System.out.println("jsonStringResponse jsonStringResponse :::::" + jsonStringResponse);
                }
            }
        }catch (Exception e) {
            e.printStackTrace(); // later add the log.error()
            return ResponseEntity.ok(new ApiResponse("ERROR", 500,e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse("SUCCESS", 200,"Json Logic generated successfully",generatedJsonLogic));
    }

    String callOpenAiApi(String jsonStringRequest) throws Exception{
        String jsonResponse = null;
        String openApiUrl = prismApiBaseUrl+"/v1/chat/completions";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(openApiUrl);
        // Set the request headers
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setHeader("Authorization", "Bearer "+prismApiKey);
        // Set the request body
        StringEntity entity = new StringEntity(jsonStringRequest);
        postRequest.setEntity(entity);
        // Execute the request
        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            HttpResponse httpResponse = response;
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // Parse the response
                jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                System.out.println("Generated JSON Logic: " + jsonResponse);
            } else {
                System.err.println("Error: " + httpResponse.getStatusLine().getStatusCode() + " - " + httpResponse.getStatusLine().getReasonPhrase());
                return null;
            }
        }catch (IOException e) {
            jsonResponse = "ERROR";
            e.printStackTrace(); // later add the log.error()
        }
        return jsonResponse;
    }
}

