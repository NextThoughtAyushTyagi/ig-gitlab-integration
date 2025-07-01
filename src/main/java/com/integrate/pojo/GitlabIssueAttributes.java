package com.integrate.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GitlabIssueAttributes {
    private Long projectId;
    private String title;
    private String description;
}
