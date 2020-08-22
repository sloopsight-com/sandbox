package com.sloopsight.sandbox.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sloopsight.sandbox.app.entity.ERole;
import com.sloopsight.sandbox.app.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    @Query("from Role _role where _role.id not in :ids")
    List<Role> findAllExcept(List<Integer> ids);

}
