package com.sloopsight.sandbox.app.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.sloopsight.sandbox.app.util.LdapContext;

public class ChainAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private LdapContext ldapContext;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO Auto-generated method stub

        if (!ldapContext.isEnabled()) {
            return super.authenticate(authentication);
        }
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                () -> messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        Optional<UserDetails> UserDetailsOpt = ldapContext.authenticate(username,
                String.valueOf(authentication.getCredentials()));

        if (!UserDetailsOpt.isPresent()) {
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));

        }
        UserDetails userDetails = UserDetailsOpt.get();

        try {

            additionalAuthenticationChecks(userDetails, (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {

        }

        Object principalToReturn = userDetails;
        return createSuccessAuthentication(principalToReturn, authentication, userDetails);
    }

}
