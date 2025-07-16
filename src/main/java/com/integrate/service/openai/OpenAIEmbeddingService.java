package com.integrate.service.openai;

import com.integrate.response.ApiResponse;
import com.integrate.service.gitlab.GitLabIssueService;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIEmbeddingService {

	@Autowired
	VectorStore vectorStore;

	public void vectorTest() {
//		List<Document> documents = List.of(
//				new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
//				new Document("The World is Big and Salvation Lurks Around the Corner"),
//				new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
//
//// Add the documents to Milvus Vector Store
//		vectorStore.add(documents);

		List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("The World").topK(1).build());
//		List<Document> documentList= vectorStore.similaritySearch(
//				SearchRequest.builder()
//						.query("The World")
////						.topK(1)
////						.similarityThreshold(0)
//						.filterExpression("author in ['john', 'jill'] && article_type == 'blog'").build());

		System.out.println("==========documentList size=================" +results.size());
		for (Document document: results) {
			System.out.println(document.toString());
		}
	}
}