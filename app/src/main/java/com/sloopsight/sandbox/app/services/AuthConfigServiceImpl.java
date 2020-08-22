package com.sloopsight.sandbox.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sloopsight.sandbox.app.dto.request.AuthConfigRequest;
import com.sloopsight.sandbox.app.entity.AuthConfig;
import com.sloopsight.sandbox.app.repo.AuthConfigRepository;

@Service
public class AuthConfigServiceImpl implements AuthConfigService {

    @Autowired
    private AuthConfigRepository authConfigRepository;

    @Override
    public Optional<AuthConfig> save(String name, AuthConfigRequest authConfigRequest) {

        Optional<AuthConfig> configOpt = authConfigRepository.findById(name);
        if (configOpt.isPresent()) {
            AuthConfig authConfig = configOpt.get();
            authConfig.setEnabled(authConfigRequest.getEnabled());
            authConfig.setConfig(authConfigRequest.getConfig());
         return   Optional.of(authConfigRepository.save(authConfig));
        }
        return Optional.empty();
    }

    @Override
    public Optional<AuthConfig> get(String name) {
        return authConfigRepository.findById(name);
    }
}
