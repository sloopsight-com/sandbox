package com.sloopsight.sandbox.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sloopsight.sandbox.app.dto.request.EndpointRequest;
import com.sloopsight.sandbox.app.entity.Endpoint;
import com.sloopsight.sandbox.app.entity.Project;
import com.sloopsight.sandbox.app.repo.EndpointRepository;
import com.sloopsight.sandbox.app.repo.ProjectRepository;

@Service
public class EndpointServiceImpl implements EndpointService {

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Optional<Endpoint> save(EndpointRequest endpointRequest) {

        Optional<Project> project = projectRepository.findById(endpointRequest.getProjectId());
        if (project.isPresent()) {
            Endpoint endpoint = new Endpoint();
            endpoint.setDescription(endpointRequest.getDescription());
            endpoint.setLogic(endpointRequest.getLogic());
            endpoint.setPath(endpointRequest.getPath());
            endpoint.setProject(project.get());
            return Optional.of(endpointRepository.save(endpoint));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Endpoint> findOne(Long id) {
        return endpointRepository.findById(id);
    }

    @Override
    public Page<Endpoint> find(Long projectId, int page, int size) {
        return endpointRepository.findAll(projectId, PageRequest.of(page, size));

    }

    @Override
    public Optional<Endpoint> delete(Long endpointId) {
        Optional<Endpoint> endpoint = endpointRepository.findById(endpointId);
        endpoint.ifPresent(a -> endpointRepository.delete(a));
        return endpoint;
    }

    @Override
    public Optional<Endpoint> updateLogic(Long id, String logic) {

        Optional<Endpoint> oldEnpointOpt = endpointRepository.findById(id);
        if (oldEnpointOpt.isPresent()) {
            Endpoint oldEndpoint = oldEnpointOpt.get();
            oldEndpoint.setLogic(logic);
            return Optional.of(endpointRepository.save(oldEndpoint));
        }
        return Optional.empty();
    }

}
