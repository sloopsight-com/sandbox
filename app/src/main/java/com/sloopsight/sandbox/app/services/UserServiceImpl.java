package com.sloopsight.sandbox.app.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sloopsight.sandbox.app.dto.request.SignupRequest;
import com.sloopsight.sandbox.app.dto.request.UserUpdationRequest;
import com.sloopsight.sandbox.app.entity.Role;
import com.sloopsight.sandbox.app.entity.User;
import com.sloopsight.sandbox.app.exceptions.BadUserRequestExecption;
import com.sloopsight.sandbox.app.repo.RoleRepository;
import com.sloopsight.sandbox.app.repo.UserRepository;
import com.sloopsight.sandbox.app.util.SecurityHelper;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Page<User> findAll(int page, int size) {
        return userRepository.findAllProjectedBy(PageRequest.of(page, size));
    }

    @Override
    public User findById(Long id) {
        // TODO Auto-generated method stub
        return userRepository.findProjectedById(id);
    }

    @Override
    public User save(SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {

            throw new BadUserRequestExecption("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {

            throw new BadUserRequestExecption("Error: Email is already in use!");
        }

        final Set<Role> roles = new HashSet<Role>();

        signUpRequest.getRole().forEach(role -> {

            Optional<Role> r = roleRepository.findByName(role.getName());
            if (r.isPresent()) {
                roles.add(r.get());
            } else {
                roles.add(role);
            }

        });

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setRoles(roles);
        return userRepository.save(user);

    }

    @Override
    public Optional<User> update(Long id, UserUpdationRequest request) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User savedUser = user.get();
            boolean updateEmail = !trimEIC(savedUser.getEmail(), request.getEmail());
            if (updateEmail) {
                if (userRepository.existsByEmail(request.getEmail())) {
                    throw new BadUserRequestExecption("Error: Email is already in use!");
                }
                savedUser.setEmail(request.getEmail());
            }

            if (StringUtils.isNotBlank(request.getPassword())) {
                if (SecurityHelper.isValidPassword(request.getPassword())) {
                    savedUser.setPassword(encoder.encode(request.getPassword()));
                } else {
                    throw new BadUserRequestExecption("Error: bad password");
                }
            }

            final Set<Role> roles = new HashSet<Role>();

            request.getRole().forEach(role -> {

                Optional<Role> r = roleRepository.findByName(role.getName());
                if (r.isPresent()) {
                    roles.add(r.get());
                } else {
                    roles.add(role);
                }

            });
            savedUser.setRoles(roles);
            return Optional.of(userRepository.save(savedUser));

        }
        return Optional.empty();
    }

    boolean trimEIC(String a, String b) {
        return StringUtils.equalsIgnoreCase(StringUtils.trim(a), StringUtils.trim(b));
    }

    @Override
    public List<Role> getAvailableRoles(Long id) {

        List<Integer> existingRoles = new ArrayList<Integer>(Arrays.asList(0));
        if (id > 0) {
            userRepository.findById(id).ifPresent(u -> {

                if (!CollectionUtils.isEmpty(u.getRoles())) {
                    u.getRoles().forEach(r -> existingRoles.add(r.getId()));
                }

            });

        }
        // TODO Auto-generated method stub
        return roleRepository.findAllExcept(existingRoles);
    }

}
