package com.sloopsight.sandbox.app.dto.request;

import javax.validation.constraints.NotBlank;

public class ProjectRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String name;

    @NotBlank
    private String openApiSpec;

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

}
