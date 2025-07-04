package com.integrate.service;

import com.integrate.pojo.gitlab.GitlabIssueAttributes;

public interface GitLabIssueService {
     String createIssue(GitlabIssueAttributes gitlabIssueAttributes);
     String fetchIssue(String projetId,String ticketId);
     String fetchDescriptionFromGitlabTicket(String projetId,String ticketId);
}
