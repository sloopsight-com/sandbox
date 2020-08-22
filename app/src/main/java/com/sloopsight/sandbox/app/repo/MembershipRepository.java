package com.sloopsight.sandbox.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sloopsight.sandbox.app.entity.MemberShipKey;
import com.sloopsight.sandbox.app.entity.UserProjects;

@Repository
public interface MembershipRepository extends JpaRepository<UserProjects, MemberShipKey> {

    @Query("from UserProjects _project where _project.key.project.id = :projectId")
    List<UserProjects> findByProjectId(Long projectId);

}
