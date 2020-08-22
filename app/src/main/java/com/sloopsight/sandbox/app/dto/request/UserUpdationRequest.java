package com.sloopsight.sandbox.app.dto.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.sloopsight.sandbox.app.entity.Role;

public class UserUpdationRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<Role> role;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

}
