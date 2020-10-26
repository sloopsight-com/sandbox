package com.sloopsight.sandbox.app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sloopsight.sandbox.app.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

    @Query("select _p from Project _p join UserProjects _u on _u.key.project.id=_p.id and _u.key.user.id =:id ")
    public Page<Project> findAll(@Param("id") Long id,Pageable pageRequest);
}