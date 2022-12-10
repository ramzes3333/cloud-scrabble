package com.aryzko.scrabble.scrabbledictionary.infrastructure.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/v3/api-docs/**").permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .antMatchers("/**").authenticated())
                .oauth2ResourceServer().jwt();
    }
}
