package com.integrate.service.gitlabImple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrate.pojo.GitlabIssueAttributes;
import com.integrate.pojo.Issue;
import com.integrate.service.IssueService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class GitlabIssueService implements IssueService {
    private static final String  PRIVATE_TOKEN = "glpat-o2-M3y2zbKTWwqfWbs_X";
    private static final String  GITLAB_API_URL_CREATE = "https://gitlab.com/api/v4/projects/{projectId}/issues";
    private static final String  GITLAB_API_URL_FETCH = "https://gitlab.com/api/v4/projects/{projectId}/issues";
    private static final String  GITLAB_API_URL_FETCH_ID = "https://gitlab.com/api/v4/projects/{projectId}/issues/{ticketId}";
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
}
