package com.integrate.mcp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrate.pojo.gitlab.GitlabIssueAttributes;
import com.integrate.pojo.gitlab.Issue;
import com.integrate.service.GitLabIssueService;
import org.springframework.ai.tool.annotation.Tool;
//import io.modelcontextprotocol.annotations.ModelContext;
//import io.modelcontextprotocol.annotations.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//@ModelContext(name = "gitlab-model", sessionId = "manual-session")
@Component
public class GitLabTool {

    @Autowired
    GitLabIssueService gitLabIssueService;

    private static final String  PRIVATE_TOKEN = "glpat-o2-M3y2zbKTWwqfWbs_X";
    private static final String GITLAB_API_URL_FETCH_ID = "https://gitlab.com/api/v4/projects/{projectId}/issues/{ticketId}";

    //@Tool(name = "fetchDescriptionFromGitlabTicket", description = "Fetch GitLab ticket description details by projectId and ticketId")
    @Tool(name="fetchDescriptionFromGitlabTicket",description = "fetch Description From Gitlab Ticket by projectId and ticketId")
    public String fetchDescriptionFromGitlabTicket(String projectId, String ticketId) {
        System.out.println("calling through mcp server tool :::::::");
       try{
            gitLabIssueService.fetchDescriptionFromGitlabTicket(projectId,ticketId);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching issue: " + e.getMessage();
        }
    }
}
