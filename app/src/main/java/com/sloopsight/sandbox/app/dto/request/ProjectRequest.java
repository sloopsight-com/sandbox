package com.sloopsight.sandbox.app.dto.request;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.sloopsight.sandbox.app.dto.response.Member;

public class ProjectRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String name;

    @NotBlank
    private String openApiSpec;

    private List<Member> members = new LinkedList<Member>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenApiSpec() {
        return openApiSpec;
    }

    public void setOpenApiSpec(String openApiSpec) {
        this.openApiSpec = openApiSpec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

}
