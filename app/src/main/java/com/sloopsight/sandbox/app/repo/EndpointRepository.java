package com.sloopsight.sandbox.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sloopsight.sandbox.app.entity.Endpoint;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    @Query(" from Endpoint _e where _e.project.id = :projectId")
    public Page<Endpoint> findAll(@Param("projectId") Long projectId, Pageable pageRequest);

    @Query(" from Endpoint _e where _e.project.id = :projectId")
    public List<Endpoint> findAllByProject(@Param("projectId") Long projectId);


    @Query(" from Endpoint _e where _e.project.id = :projectId and _e.method= :method")
    public List<Endpoint> findAllByProjectAndMethod(@Param("projectId") Long projectId,@Param("method") String method);


    @Query(" from Endpoint _e where _e.project.id = :projectId and _e.path= :path and _e.method= :method")
    public Optional<Endpoint> findExisting(@Param("projectId") Long projectId,@Param("method") String method,@Param("path") String path);

}
