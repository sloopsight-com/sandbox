package com.sloopsight.sandbox.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.sloopsight.sandbox.app.dto.request.ProjectRequest;
import com.sloopsight.sandbox.app.dto.response.Member;
import com.sloopsight.sandbox.app.entity.Project;

public interface ProjectService {

    Optional<Project> save(ProjectRequest project);

    Optional<Project> findOne(Long id);

    Page<Project> find(String userId, int page, int size);

    Optional<Project> delete(Long id);

    Optional<Project> update(Long projectId, ProjectRequest projectRequest);

    Optional<JsonNode> getDocs(Long projectId);

    List<Member> getExisitingMembers(Long id);

    List<Member> getAvailableMembers(Long id);

}
