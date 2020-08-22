package com.sloopsight.sandbox.app.util;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sloopsight.sandbox.app.entity.ERole;

public class SecurityHelper {

    public static String loggedInUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static boolean isGranted(ERole role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equalsIgnoreCase(role.name())) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public static boolean isValidPassword(String password) {
        if (StringUtils.isNoneBlank(password)) {
            return password.length() > 5 && password.length() < 50;
        }
        return false;
    }

}
