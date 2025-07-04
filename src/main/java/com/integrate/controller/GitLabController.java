package com.integrate.controller;

import com.integrate.pojo.gitlab.GitlabIssueAttributes;
import com.integrate.response.ApiResponse;
import com.integrate.service.GitLabIssueService;
import com.integrate.service.openAi.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/gitlab")
public class GitLabController {

    @Autowired
    GitLabIssueService gitLabIssueService;

    @Autowired
    OpenAiService openAiService;

    @PostMapping("/create-issue")
    ResponseEntity<String> createGitLabIssue(@RequestBody GitlabIssueAttributes gitlabIssueAttributes){
        System.out.println("Inside createGitLabIssue  :::::");
        System.out.println("gitlabIssueAttributes getTitle ------ "+gitlabIssueAttributes.getTitle());
        System.out.println("gitlabIssueAttributes getDescription ------ "+gitlabIssueAttributes.getDescription());
        System.out.println("gitlabIssueAttributes getProjectId ------ "+gitlabIssueAttributes.getProjectId());
        String gitLabResponse = gitLabIssueService.createIssue(gitlabIssueAttributes);
        System.out.println("gitLabResponse --->>>>>> "+gitLabResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket Created");
    }

    @GetMapping("/fetch-issue/{projectId}/{ticketId}")
    ResponseEntity<String> fetchGitlabIssue(@PathVariable("projectId") String projectId,@PathVariable("ticketId") String ticketId){
        System.out.println("gitlabIssueAttributes ticketId ------ "+ticketId);
        System.out.println("gitlabIssueAttributes projectId ------ "+projectId);
        String gitLabResponse = gitLabIssueService.fetchIssue(projectId,ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(gitLabResponse);
    }

    @GetMapping("/fetchDescriptionFromGitlabTicket")
    ResponseEntity fetchDescriptionFromGitlabTicket(@RequestParam("projectId") String projectId,@RequestParam("ticketId") String ticketId){
        try {
            String gitLabResponse = gitLabIssueService.fetchDescriptionFromGitlabTicket(projectId, ticketId);
            return openAiService.generateJsonForTheGivenDescription(gitLabResponse);
        }catch (Exception e){
            e.printStackTrace();  // later We will use a log.error()
            return ResponseEntity.ok(new ApiResponse("ERROR", 500,e.getMessage()));
        }
    }

    @PostMapping("/generateJsonForTheGivenDescription")
    public ResponseEntity generateJsonForTheGivenDescription(@RequestBody Map<String, String> request)  throws Exception{
        try {
            // for proper respopnse format need to implement the common Response class with :- status,code,message & object
            String userMessage = request.get("userMessage");
            return openAiService.generateJsonForTheGivenDescription(userMessage);
        } catch (Exception e) {
            e.printStackTrace();  // later We will use a log.error()
            return ResponseEntity.ok(new ApiResponse("ERROR", 500,e.getMessage()));
        }
    }
}
