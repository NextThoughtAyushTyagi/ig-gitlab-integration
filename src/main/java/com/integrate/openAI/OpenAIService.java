package com.integrate.openAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    ChatClient.Builder chatClientBuilder;
    OpenAIService(@Autowired ChatClient.Builder chatClientBuilder){
        this.chatClientBuilder = chatClientBuilder;
    }

    public void openAIChatClient(){
		System.out.println("inside openAIChatClient --------- ");
        ChatClient chatClient = chatClientBuilder.build();
		pt_system_prompting_2_openAI(chatClient);
    }



	public void pt_system_prompting_2_openAI(ChatClient chatClient) {
		OpenAiChatOptions openAiOptions = OpenAiChatOptions.builder()
				.model("gpt-4o")
				//.temperature(0.1)
				//.frequencyPenalty(0.5)      // OpenAI-specific parameter
				//.presencePenalty(0.3)       // OpenAI-specific parameter
				.responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA,null))  // OpenAI-specific JSON mode
				.seed(42)                   // OpenAI-specific deterministic generation
				.build();

		String movieReview = chatClient
				.prompt("The logic calculates the sum of 2 variables 'crp', 'baseRate' with 2.85 constant value")
				.system("Generate a compact and standard JSON structure and ensure the JSON output uses compact logic without redundant keys/statements and follows jamsesso/json-logic-java & js-logic npm 2.0.2 package format compatible operators only")
				.user("You are a JSON generator who generates executable JSON without any explanations, formatting, or additional characters such as newlines or spaces.Use only executable operators for example use 'if' instead of 'conditions', 'else' instead of 'then' as JSON logic requires proper compatible JSON with simple operators like if, else, switch-case, in etc")

				.options(openAiOptions)
				.call()
				.content();

		System.out.println("Output: " + movieReview);

	}
}
