package com.sloopsight.sandbox.app.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sloopsight.sandbox.app.dto.request.ProjectRequest;
import com.sloopsight.sandbox.app.entity.ERole;
import com.sloopsight.sandbox.app.entity.Endpoint;
import com.sloopsight.sandbox.app.entity.Project;
import com.sloopsight.sandbox.app.entity.User;
import com.sloopsight.sandbox.app.exceptions.BadSpecException;
import com.sloopsight.sandbox.app.repo.EndpointRepository;
import com.sloopsight.sandbox.app.repo.ProjectRepository;
import com.sloopsight.sandbox.app.repo.UserRepository;
import com.sloopsight.sandbox.app.util.Mapper;
import com.sloopsight.sandbox.app.util.SecurityHelper;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

@Component
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private Mapper mapper;

    private Endpoint createOrUpdateEndpoint(Project p, String desc, String method, String path) {

        Optional<Endpoint> ep = endpointRepository.findExisting(p.getId(), method, path);
        if (ep.isPresent()) {
            Endpoint endpoint = ep.get();
            endpoint.setDescription(desc);
            endpoint.setPath(path);
            endpoint.setMethod(method);
            endpoint.setProject(p);
            return endpointRepository.save(endpoint);
        } else {

            Endpoint endpoint = new Endpoint();
            endpoint.setDescription(desc);
            endpoint.setPath(path);
            endpoint.setMethod(method);
            endpoint.setProject(p);
            return endpointRepository.save(endpoint);
        }
    }

    @Override
    public Optional<Project> save(ProjectRequest projectRequest) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(user).map(u -> {
            Project project = new Project();
            project.setName(projectRequest.getName());
            project.setOpenApiSpec(projectRequest.getOpenApiSpec());
            Project savedProject = projectRepository.save(project);
            updateEndpoint(savedProject);
            return savedProject;
        });

    }

    private void updateEndpoint(Project project) {
        try {

            Set<Endpoint> orphanEp = new HashSet<Endpoint>();
            List<Endpoint> existingEp = endpointRepository.findAllByProject(project.getId());
            Set<Endpoint> currentEp = new HashSet<Endpoint>();

            SwaggerParseResult result = new OpenAPIV3Parser().readContents(project.getOpenApiSpec());
            OpenAPI openAPI = result.getOpenAPI();

            if (openAPI != null) {

                for (String path : openAPI.getPaths().keySet()) {
                    PathItem pi = openAPI.getPaths().get(path);
                    if (pi.getPut() != null) {
                        currentEp.add(createOrUpdateEndpoint(project, pi.getPut().getSummary(), "put", path));
                    }
                    if (openAPI.getPaths().get(path).getGet() != null) {
                        currentEp.add(createOrUpdateEndpoint(project, pi.getGet().getSummary(), "get", path));
                    }
                    if (openAPI.getPaths().get(path).getDelete() != null) {
                        currentEp.add(createOrUpdateEndpoint(project, pi.getDelete().getSummary(), "delete", path));
                    }
                    if (openAPI.getPaths().get(path).getPost() != null) {
                        currentEp.add(createOrUpdateEndpoint(project, pi.getPost().getSummary(), "post", path));
                    }
                }

            } else {
                throw new BadSpecException(mapper.toJson(result.getMessages()));
            }

            existingEp.forEach(ep -> {
                if (!currentEp.contains(ep)) {
                    orphanEp.add(ep);
                }
            });
            if (!CollectionUtils.isEmpty(orphanEp)) {
                endpointRepository.deleteAll(orphanEp);
            }
        } catch (Exception e) {
            throw new BadSpecException(e.getMessage(), e);
        }

    }

    @Override
    public Optional<Project> update(Long projectId, ProjectRequest projectRequest) {
        Optional<Project> project = projectRepository.findById(projectId);

        return project.map(p -> {
            p.setName(projectRequest.getName());
            p.setOpenApiSpec(projectRequest.getOpenApiSpec());
            updateEndpoint(p);
            return projectRepository.save(p);
        });
    }

    @Override
    public Optional<Project> delete(Long id) {

        Optional<Project> project = projectRepository.findById(id);
        project.ifPresent(a -> projectRepository.delete(a));
        return project;
    }

    @Override
    public Optional<Project> findOne(Long id) {

        return projectRepository.findById(id);
    }

    @Override
    public Page<Project> find(String userId, int page, int size) {

        if (SecurityHelper.isGranted(ERole.ROLE_ADMIN)) {
            return projectRepository.findAll(PageRequest.of(page, size));
        } else {
            Optional<User> user = userRepository.findByUsername(userId);
            if (user.isPresent()) {
                return projectRepository.findAll(user.get().getId(), PageRequest.of(page, size));
            } else {
                return Page.empty();
            }
        }
    }

    @Override
    public Optional<JsonNode> getDocs(Long projectId) {
        return projectRepository.findById(projectId).map(p -> {
            JsonNode json = mapper.readTree(p.getOpenApiSpec());
            JsonNode node = json.get("servers").get(0);
            ((ObjectNode) node).put("url", "camel/exec/" + projectId);
            ((ObjectNode) node).put("description", p.getDescription());
            return json;

        });
    }

}