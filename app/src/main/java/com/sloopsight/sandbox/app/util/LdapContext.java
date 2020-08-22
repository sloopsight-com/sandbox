package com.sloopsight.sandbox.app.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sloopsight.sandbox.app.entity.AuthConfig;
import com.sloopsight.sandbox.app.entity.ERole;
import com.sloopsight.sandbox.app.entity.Role;
import com.sloopsight.sandbox.app.entity.User;
import com.sloopsight.sandbox.app.repo.AuthConfigRepository;
import com.sloopsight.sandbox.app.repo.RoleRepository;
import com.sloopsight.sandbox.app.repo.UserRepository;
import com.sloopsight.sandbox.app.services.UserDetailsImpl;

@Component
public class LdapContext {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private LdapTemplate ldapTemplate = null;
    private static final String LDAP = "ldap";

    private boolean enabled = false;
    private boolean anonymous = false;

    private Map<String, Object> config = new HashMap<String, Object>();

    @Autowired
    private AuthConfigRepository configRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository repository;

    public boolean isEnabled() {
        return enabled;
    }

    @PostConstruct
    public void setUp() {

        Optional<AuthConfig> opt = configRepository.findById(LDAP);
        if (opt.isPresent()) {
            AuthConfig authConfig = opt.get();
            this.enabled = authConfig.getEnabled();
            if (enabled) {
                this.config = authConfig.getConfig();
                LdapContextSource contextSource = new LdapContextSource();
                contextSource.setUrl((String) authConfig.getConfig().get("url"));
                this.anonymous = BooleanUtils.toBoolean((Boolean) authConfig.getConfig().get("anonymous"));
                if (!anonymous) {
                    contextSource.setUserDn((String) authConfig.getConfig().get("bindDn"));
                    contextSource.setPassword((String) authConfig.getConfig().get("bindPassword"));
                }
                contextSource.afterPropertiesSet();
                this.ldapTemplate = new LdapTemplate(contextSource);
            }
        }
    }

    public Optional<UserDetails> authenticate(String user, String password) {
        Optional<User> optUser = userRepository.findByUsername(user);

        try {
            String searchBase = (String) config.get("searchBase");
            String searchFilterTemplate = (String) config.get("searchFilter");
            Map<String, Object> value = new HashMap<String, Object>();
            value.put("username", user);
            String searchFilter = StringSubstitutor.replace(searchFilterTemplate, value);
            String ldapPassword = StringUtils.EMPTY;
            String email = StringUtils.EMPTY;
            String emailField = (String) config.get("emailField");
            if (anonymous) {
                List<String> cn = ldapTemplate.search(searchBase, searchFilter,
                        (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());

                if (!CollectionUtils.isEmpty(cn)) {
                    logger.info("Load credentials of " + cn);
                    LdapContextSource localContextSource = new LdapContextSource();
                    localContextSource.setUrl((String) config.get("url"));
                    localContextSource.setUserDn("cn=" + cn.get(0) + "," + searchBase);
                    localContextSource.setPassword(password);
                    localContextSource.afterPropertiesSet();
                    LdapTemplate localLdapTemplate = new LdapTemplate(localContextSource);

                    List<byte[]> result = localLdapTemplate.search(searchBase, searchFilter,
                            (AttributesMapper<byte[]>) attrs -> (byte[]) attrs.get("userPassword").get());
                    if (!CollectionUtils.isEmpty(result)) {
                        ldapPassword = new String(result.get(0));
                        email = localLdapTemplate.search(searchBase, searchFilter,
                                (AttributesMapper<String>) attrs -> (String) attrs.get(emailField).get()).get(0);
                    }
                }

            } else {

                List<byte[]> result = ldapTemplate.search(searchBase, searchFilter,
                        (AttributesMapper<byte[]>) attrs -> (byte[]) attrs.get("userPassword").get());
                if (!CollectionUtils.isEmpty(result)) {
                    ldapPassword = new String(result.get(0));
                    email = ldapTemplate.search(searchBase, searchFilter,
                            (AttributesMapper<String>) attrs -> (String) attrs.get(emailField).get()).get(0);
                }

            }

            if (StringUtils.isNotEmpty(ldapPassword)) {

                if (optUser.isPresent()) {
                    User existingUser = optUser.get();
                    existingUser.setPassword(ldapPassword);
                    existingUser.setEmail(email);
                    userRepository.save(existingUser);
                    return Optional.of(UserDetailsImpl.build(existingUser));
                } else {
                    // Auto register
                    User autoUser = new User();
                    autoUser.setEmail(email);
                    autoUser.setUsername(user);
                    autoUser.setPassword(ldapPassword);
                    Optional<Role> roleOpt = repository.findByName(ERole.ROLE_USER);
                    if (roleOpt.isPresent()) {
                        autoUser.setRoles(new HashSet<Role>(Arrays.asList(roleOpt.get())));
                    }
                    User savedUser = userRepository.save(autoUser);
                    return Optional.of(UserDetailsImpl.build(savedUser));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (optUser.isPresent()) {
            return Optional.of(UserDetailsImpl.build(optUser.get()));
        } else {
            return Optional.empty();
        }
    }

}
