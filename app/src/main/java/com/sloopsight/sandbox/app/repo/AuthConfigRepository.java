package com.sloopsight.sandbox.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sloopsight.sandbox.app.entity.AuthConfig;

public interface AuthConfigRepository extends JpaRepository<AuthConfig, String> {

}
