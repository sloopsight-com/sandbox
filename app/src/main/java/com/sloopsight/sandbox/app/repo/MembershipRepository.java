package com.sloopsight.sandbox.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sloopsight.sandbox.app.entity.MemberShipKey;
import com.sloopsight.sandbox.app.entity.UserProjects;

@Repository
public interface MembershipRepository extends JpaRepository<UserProjects, MemberShipKey> {

}
