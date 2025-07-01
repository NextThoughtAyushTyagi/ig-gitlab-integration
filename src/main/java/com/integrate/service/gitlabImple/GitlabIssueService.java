package com.integrate.service.gitlabImple;

import com.integrate.pojo.GitlabIssueAttributes;
import com.integrate.service.IssueService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class GitlabIssueService implements IssueService {
    private static final String  KEY = "";
    private static final String  GITLAB_API_URL_CREATE = "https://gitlab.com/api/v4/projects/{projectId}/issues";
    private static final String  GITLAB_API_URL_FETCH = "https://gitlab.com/api/v4/projects/{projectId}/issues";
    @Override
    public String createIssue(GitlabIssueAttributes gitlabIssueAttributes) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", KEY);
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
    public String fetchIssue(GitlabIssueAttributes gitlabIssueAttributes) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", KEY);
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
}
