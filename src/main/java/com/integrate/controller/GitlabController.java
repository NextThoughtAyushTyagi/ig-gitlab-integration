package com.integrate.controller;

import com.integrate.pojo.GitlabIssueAttributes;
import com.integrate.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gitlab")
public class GitlabController {

    @Autowired
    IssueService issueService;
    @PostMapping("/create-issue")
    ResponseEntity<String> createGitLabIssue(@RequestBody GitlabIssueAttributes gitlabIssueAttributes){
        System.out.println("gitlabIssueAttributes getTitle ------ "+gitlabIssueAttributes.getTitle());
        System.out.println("gitlabIssueAttributes getDescription ------ "+gitlabIssueAttributes.getDescription());
        System.out.println("gitlabIssueAttributes getProjectId ------ "+gitlabIssueAttributes.getProjectId());
        String gitLabResponse = issueService.createIssue(gitlabIssueAttributes);
        System.out.println("gitLabResponse --->>>>>> "+gitLabResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket Created");
    }

    @GetMapping("/fetch-issue/{projectId}/{ticketId}")
    ResponseEntity<String> fetchGitlabIssue(@PathVariable("projectId") String projectId,@PathVariable("ticketId") String ticketId){
        System.out.println("gitlabIssueAttributes ticketId ------ "+ticketId);
        System.out.println("gitlabIssueAttributes projectId ------ "+projectId);
        String gitLabResponse = issueService.fetchIssue(projectId,ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(gitLabResponse);
    }

}
