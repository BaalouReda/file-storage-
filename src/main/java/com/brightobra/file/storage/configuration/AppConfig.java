package com.brightobra.file.storage.configuration;


import com.brightobra.file.storage.configuration.security.OpenBSDBCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new OpenBSDBCryptPasswordEncoder();
    }
}
