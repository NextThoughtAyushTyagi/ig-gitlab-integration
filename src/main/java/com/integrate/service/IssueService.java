package com.integrate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrate.pojo.GitlabIssueAttributes;

public interface IssueService {
     String createIssue(GitlabIssueAttributes gitlabIssueAttributes);
     String fetchIssue(String projetId,String ticketId);
}
