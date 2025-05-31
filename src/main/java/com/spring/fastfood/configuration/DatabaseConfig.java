package com.spring.fastfood.configuration;


import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Role;
import com.spring.fastfood.model.User;
import com.spring.fastfood.model.UserHasRole;
import com.spring.fastfood.repository.RoleRepository;
import com.spring.fastfood.repository.UserRepository;
import com.spring.fastfood.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public ApplicationRunner applicationRunner (){
        return arguments -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                Role role = roleRepository.findByRoleName("ADMIN")
                        .orElseThrow(() -> new ResourceNotFoundException("ADMIN role not found"));

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(user);

                UserHasRole userHasRole = UserHasRole.builder()
                        .role(role)
                        .user(user)
                        .build();
                userRoleRepository.save(userHasRole);
                log.info("user admin has been created with default password please change it");
            }
        };
    }
}
