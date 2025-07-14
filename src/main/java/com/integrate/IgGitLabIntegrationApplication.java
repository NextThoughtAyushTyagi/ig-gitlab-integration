package com.integrate;

import org.gitlab4j.api.GitLabApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IgGitLabIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgGitLabIntegrationApplication.class, args);
	}

	@Bean
	public GitLabApi gitLabClient(@Value("${gitlab.token}") String token,
								  @Value("${gitlab.base-url}") String baseUrl) {
		return new GitLabApi(baseUrl, token);
	}

	@Bean
	public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
		return ChatClient.create(chatModel);
	}

}
