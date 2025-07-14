package com.integrate.service.gitlab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrate.pojo.gitlab.GitlabIssueAttributes;
import com.integrate.pojo.gitlab.Issue;
import com.integrate.response.ApiResponse;
import com.integrate.service.GitLabIssueService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.IssuesApi;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.models.Project;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class GitLabIssueServiceImpl implements GitLabIssueService {
    private static final String  PRIVATE_TOKEN = "glpat-JC5MCCEFpz-UJrFgY5HZ";
    private static final String  GITLAB_API_URL_CREATE = "https://gitlab.com/api/v4/projects/{projectId}/issues";
    private static final String  GITLAB_API_URL_FETCH = "https://gitlab.com/api/v4/projects/{projectId}/issues";
    private static final String  GITLAB_API_URL_FETCH_ID = "https://gitlab.com/api/v4/projects/{projectId}/issues/{ticketId}";

    @Autowired
    GitLabApi gitLabApi;

    @Override
    public String createIssue(GitlabIssueAttributes gitlabIssueAttributes) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", PRIVATE_TOKEN);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", gitlabIssueAttributes.getTitle());
        map.add("description", gitlabIssueAttributes.getDescription());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    GITLAB_API_URL_CREATE,
                    request,
                    String.class,
                    gitlabIssueAttributes.getProjectId()
            );
            return response.getBody();
        } catch (Exception e) {
            //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed: " + e.getMessage());
            return "Error";
        }
    }

    @Override
    public String fetchIssue(String projetId,String ticketId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", PRIVATE_TOKEN);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    GITLAB_API_URL_FETCH_ID,
                    HttpMethod.GET,
                    request,
                    String.class,
                    projetId,
                    ticketId);
            String stringResponse =  response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            Issue issue = mapper.readValue(stringResponse, Issue.class);
            System.out.println("issue.getDescription() --->>> "+issue.getDescription());
            Pattern pattern = Pattern.compile("###(.*?)###");
            Matcher matcher = pattern.matcher(issue.getDescription());

            if (matcher.find()) {
                String extracted = matcher.group(1);  // Get the first capture group
                System.out.println("Extracted text: " + extracted.trim());
            } else {
                System.out.println("No match found.");
            }
            return stringResponse;
        } catch (Exception e) {
            //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed: " + e.getMessage());
            e.printStackTrace();
            return "Error";
        }
    }
    @Override
    public String fetchDescriptionFromGitlabTicket (Long ticketId) throws GitLabApiException {
        IssuesApi issuesApi = gitLabApi.getIssuesApi();
        org.gitlab4j.api.models.Issue issue =issuesApi.getIssue("214", Long.valueOf(ticketId));
        System.out.println("Issue => "+issue.getTitle());
        System.out.println("Desc => "+issue.getDescription());
        return issue.getDescription();
    }

    @Tool(name = "gl_fetch_project", description = "Fetch all the Project from gitlab")
    public List<org.gitlab4j.api.models.Issue> getIssue() throws Exception {
        IssuesApi issuesApi = gitLabApi.getIssuesApi();
        return issuesApi.getIssues();
    }

}
