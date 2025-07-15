package com.integrate.service.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.IssuesApi;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GitLabIssueServiceImpl implements GitLabIssueService {
    @Autowired
    GitLabApi gitLabApi;

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
