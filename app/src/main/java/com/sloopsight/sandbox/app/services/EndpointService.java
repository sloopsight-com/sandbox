package com.sloopsight.sandbox.app.services;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.sloopsight.sandbox.app.dto.request.EndpointRequest;
import com.sloopsight.sandbox.app.entity.Endpoint;

public interface EndpointService {

    Optional<Endpoint> save(EndpointRequest project);

    Optional<Endpoint> findOne(Long id);

    Page<Endpoint> find(Long userId, int page, int size);

    Optional<Endpoint> delete(Long endpointId);

    Optional<Endpoint> updateLogic(Long id, String logic);
}
