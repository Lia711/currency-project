package com.example.CurrencyApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager detailsManager() {
        UserDetails user = User.builder().username("user").password("{bcrypt}$2a$10$2I0wCl9bbcdtxmq8AKDQx.7dkpzYCn2YHZJakBOcLv0hEEWyujvc6").roles("USER").build();
        UserDetails rob = User.builder().username("rob").password("{bcrypt}$2a$10$2I0wCl9bbcdtxmq8AKDQx.7dkpzYCn2YHZJakBOcLv0hEEWyujvc6").roles("USER", "COACH").build();
        UserDetails sam = User.builder().username("sam").password("{bcrypt}$2a$10$2I0wCl9bbcdtxmq8AKDQx.7dkpzYCn2YHZJakBOcLv0hEEWyujvc6").roles("USER", "COACH").build();
        UserDetails lia = User.builder().username("lia").password("{bcrypt}$2a$10$2I0wCl9bbcdtxmq8AKDQx.7dkpzYCn2YHZJakBOcLv0hEEWyujvc6").roles("USER", "COACH", "MANAGER").build();
        return new InMemoryUserDetailsManager(user, rob, sam, lia);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers(HttpMethod.GET, "/currencies").hasAnyRole("USER", "COACH", "MANAGER")
                                .antMatchers(HttpMethod.GET, "/latest/**").hasAnyRole("USER", "COACH", "MANAGER")
                                .antMatchers(HttpMethod.GET, "/compare/**").hasAnyRole("COACH", "MANAGER")
                                .antMatchers(HttpMethod.GET, "/readjson/**").hasAnyRole("MANAGER")
                                .antMatchers(HttpMethod.GET, "/createjson/**").hasAnyRole("MANAGER")
                                .anyRequest().authenticated()
                )
                .httpBasic();
        return http.build();
    }
}
