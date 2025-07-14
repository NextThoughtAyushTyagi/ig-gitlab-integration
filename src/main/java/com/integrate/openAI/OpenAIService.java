package com.integrate.openAI;

import com.integrate.response.ApiResponse;
import com.integrate.service.GitLabIssueService;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

	@Autowired
	GitLabIssueService gitLabIssueService;

	@Autowired
	ChatClient openAiChatClient;

	public ResponseEntity fetchJsonLogicViaOpenAI(String ticketId, String prompt) throws GitLabApiException {
		System.out.println("inside openAIChatClient --------- ");

		if (!ticketId.isEmpty() && !ticketId.isBlank()) {
			String issueDescription = gitLabIssueService.fetchDescriptionFromGitlabTicket(Long.valueOf(ticketId));
			if (!issueDescription.isEmpty() && !issueDescription.isBlank()) {
				prompt = issueDescription;
			}
		}

		ChatClient chatClient = openAiChatClient.mutate().build();
		OpenAiChatOptions openAiOptions = OpenAiChatOptions.builder()
				.model("gpt-4o")
				//.temperature(0.1)
				//.frequencyPenalty(0.5)      // OpenAI-specific parameter
				//.presencePenalty(0.3)       // OpenAI-specific parameter
				.responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA,null))  // OpenAI-specific JSON mode
				.seed(42)                   // OpenAI-specific deterministic generation
				.build();

		String generatedJsonLogic = chatClient
				.prompt(prompt)
				.system("Generate a compact and standard JSON structure and ensure the JSON output uses compact logic without redundant keys/statements and follows jamsesso/json-logic-java & js-logic npm 2.0.2 package format compatible operators only")
				.user("You are a JSON generator who generates executable JSON without any explanations, formatting, or additional characters such as newlines or spaces.Use only executable operators for example use 'if' instead of 'conditions', 'else' instead of 'then' as JSON logic requires proper compatible JSON with simple operators like if, else, switch-case, in etc")

				.options(openAiOptions)
				.call()
				.content();

		System.out.println("Output: " + generatedJsonLogic);
		Map<String, String> result = new HashMap<>(2);
		result.put("prompt", prompt);
		result.put("jsonLogic", generatedJsonLogic);
		return ResponseEntity.ok(new ApiResponse("SUCCESS", 200, "Json Logic generated successfully", result));

	}
}