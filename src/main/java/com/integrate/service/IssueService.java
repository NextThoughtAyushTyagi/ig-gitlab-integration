package com.integrate.service;

import com.integrate.pojo.GitlabIssueAttributes;

public interface IssueService {
     String createIssue(GitlabIssueAttributes gitlabIssueAttributes);
     String fetchIssue(GitlabIssueAttributes gitlabIssueAttributes);
}
