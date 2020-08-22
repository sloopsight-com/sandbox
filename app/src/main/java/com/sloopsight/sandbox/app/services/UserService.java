package com.sloopsight.sandbox.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.sloopsight.sandbox.app.dto.request.SignupRequest;
import com.sloopsight.sandbox.app.dto.request.UserUpdationRequest;
import com.sloopsight.sandbox.app.entity.Role;
import com.sloopsight.sandbox.app.entity.User;

public interface UserService {

    public User findById(Long id);

    public Optional<User> update(Long id, UserUpdationRequest request);

    public Page<User> findAll(int page, int size);

    public User save(SignupRequest signUpRequest);

    public List<Role> getAvailableRoles(Long id);

}
