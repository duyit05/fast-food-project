package com.spring.fastfood.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({"com.spring.fastfood"})
public class DatabaseConfig {
}
