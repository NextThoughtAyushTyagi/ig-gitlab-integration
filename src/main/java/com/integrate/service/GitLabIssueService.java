package com.integrate.service;

import com.integrate.pojo.gitlab.GitlabIssueAttributes;
import com.integrate.response.ApiResponse;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

public interface GitLabIssueService {
     String fetchDescriptionFromGitlabTicket(Long ticketId) throws GitLabApiException;
}
