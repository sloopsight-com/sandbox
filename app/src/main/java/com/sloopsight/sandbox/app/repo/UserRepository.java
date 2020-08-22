package com.sloopsight.sandbox.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sloopsight.sandbox.app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("from User _usr where _usr.id not in :ids")
    List<User> findUserNotIn(@Param("ids") List<Long> ids);

    Page<User> findAllProjectedBy(Pageable pageable);

    User findProjectedById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
