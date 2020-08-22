package com.sloopsight.sandbox.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sloopsight.sandbox.app.entity.AuthConfig;
import com.sloopsight.sandbox.app.entity.ERole;
import com.sloopsight.sandbox.app.entity.Role;
import com.sloopsight.sandbox.app.entity.User;
import com.sloopsight.sandbox.app.repo.AuthConfigRepository;
import com.sloopsight.sandbox.app.repo.UserRepository;

@EnableTransactionManagement
@EntityScan(basePackages = { "com.sloopsight.sandbox.app.entity" })
@EnableJpaRepositories({ "com.sloopsight.sandbox.app.repo" })
@ComponentScan({ "com.sloopsight.sandbox" })
@SpringBootApplication
public class Bootstrap {

    @Value("${default.user}")
    private String defaultUser;

    @Value("${default.password}")
    private String defaultPassword;

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Bean
    public ServletRegistrationBean<?> camelServletRegistrationBean() {
        ServletRegistrationBean<?> registration = new ServletRegistrationBean<>(new CamelHttpTransportServlet(), "/camel/*");
        registration.setName("CamelServlet");
        return registration;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthConfigRepository authConfigRepository;

    @PostConstruct
    public void init() {

        Optional<User> userOptional = userRepository.findByUsername(defaultUser);
        if (!userOptional.isPresent()) {

            User user = new User();
            user.setUsername(defaultUser);
            user.setPassword(defaultPassword);

            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);

            user.setRoles(new HashSet<Role>(Arrays.asList(userRole, adminRole)));
            user.setEmail("admin@sandbox.sloopsight.com");
            userRepository.save(user);

        }
        Optional<AuthConfig> authConfigOptional = authConfigRepository.findById("ldap");
        if (!authConfigOptional.isPresent()) {
            AuthConfig authConfig = new AuthConfig();

            authConfig.setName("ldap");
            authConfig.setEnabled(false);
            authConfigRepository.save(authConfig);
        }
    }
}
