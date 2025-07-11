package com.integrate;

import com.integrate.mcp.server.GitLabTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class IgGitLabIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgGitLabIntegrationApplication.class, args);
	}

	// for STDIO client server: to give query from cluade/cursor/spring client mcp
	@Bean
	ToolCallbackProvider gitLabTools() {
		return MethodToolCallbackProvider
				.builder()
				.toolObjects(new GitLabTool())
				.build();
	}

	@Bean
	List<ToolCallback> getTools(){
		return List.of(ToolCallbacks.from(new GitLabTool()));
	}
}
