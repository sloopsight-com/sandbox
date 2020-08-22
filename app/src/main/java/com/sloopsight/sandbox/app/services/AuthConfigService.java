package com.sloopsight.sandbox.app.services;

import java.util.Optional;

import com.sloopsight.sandbox.app.dto.request.AuthConfigRequest;
import com.sloopsight.sandbox.app.entity.AuthConfig;

public interface AuthConfigService {

    Optional<AuthConfig> save(String name, AuthConfigRequest authConfig);

    Optional<AuthConfig> get(String name);

}
