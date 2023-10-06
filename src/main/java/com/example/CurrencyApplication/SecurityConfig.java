package com.example.CurrencyApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager detailsManager(){
        UserDetails user = User.builder().username("user").password("{noop}currency123").roles("USER").build();
        UserDetails rob = User.builder().username("rob").password("{noop}currency123").roles("USER", "COACH").build();
        UserDetails sam = User.builder().username("sam").password("{noop}currency123").roles("USER", "COACH").build();
        UserDetails lia = User.builder().username("lia").password("{noop}currency123").roles("USER", "COACH", "MANAGER").build();
        return new InMemoryUserDetailsManager(user, rob, sam, lia);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/currencies").hasAnyRole("USER", "COACH", "MANAGER")

        );
    }
}
